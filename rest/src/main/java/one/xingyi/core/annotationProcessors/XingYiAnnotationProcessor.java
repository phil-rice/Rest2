package one.xingyi.core.annotationProcessors;

import lombok.RequiredArgsConstructor;
import one.xingyi.core.annotations.*;
import one.xingyi.core.codeDom.*;
import one.xingyi.core.filemaker.*;
import one.xingyi.core.monad.CompletableFutureDefn;
import one.xingyi.core.monad.MonadDefn;
import one.xingyi.core.names.*;
import one.xingyi.core.sdk.IXingYiResourceDefn;
import one.xingyi.core.sdk.IXingYiViewDefn;
import one.xingyi.core.utils.*;
import one.xingyi.core.validation.Result;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.function.Function;

import one.xingyi.core.validation.Valid;

@RequiredArgsConstructor
public class XingYiAnnotationProcessor extends AbstractProcessor {
    final IServerNames names = IServerNames.simple(IPackageNameStrategy.simple, IClassNameStrategy.simple);
    final IElementsToMapOfViewDefnToView elementsToMapOfViewDefnToView = IElementsToMapOfViewDefnToView.simple(names);

    private Types typeUtils;
    private Elements elementUtils;
    private Filer filer;
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        typeUtils = processingEnv.getTypeUtils();
        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
        processingEnv.getOptions();
    }

    static <T extends Element> Comparator<T> comparator() {return (a, b) -> a.asType().toString().compareTo(b.asType().toString());}

    boolean emptyOr(String s, Function<String, Boolean> fn) {return s.isEmpty() || fn.apply(s);}
    Valid<ElementFail, TypeElement> checkRootUrlStartsWithHost = check(e -> emptyOr(e.getAnnotation(Resource.class).rootUrl(), url -> url.startsWith("{host}")), e -> "Root Url needs to start with {host}");
    Valid<ElementFail, TypeElement> checkRootUrlHasId = check(e -> emptyOr(e.getAnnotation(Resource.class).rootUrl(), url -> url.contains("{id}")), e -> "Root Url needs to contain {id}");

    Valid<ElementFail, TypeElement> check(Function<TypeElement, Boolean> checkFn, Function<TypeElement, String> message) {return Valid.<String, ElementFail, TypeElement>check(checkFn, message, (e, s) -> new ElementFail(s, e));}

    Valid<ElementFail, TypeElement> checkElementIsAnInterface = check(e -> e.getKind() == ElementKind.INTERFACE, e -> "Must be an interface");
    Valid<ElementFail, TypeElement> checkNameStartsWithI = check(e -> e.getSimpleName().toString().startsWith("I"), e -> "Name must start with an I");
    Valid<ElementFail, TypeElement> checkNameEndsWithDefn = check(e -> e.getSimpleName().toString().endsWith("Defn"), e -> "Name must end with Defn");
    Valid<ElementFail, TypeElement> checkImplements(String className) {return check(e -> Lists.find(e.getInterfaces(), i -> i.toString().startsWith(className)).isPresent(), e -> "Must extend " + className);}
    Valid<ElementFail, TypeElement> initialTypeElementChecks(Class<?> obligatoryInterface) {
        return Valid.compose(checkElementIsAnInterface, checkNameStartsWithI, checkNameEndsWithDefn, checkImplements(obligatoryInterface.getName()));
    }

    Valid<ElementFail, TypeElement> initialResourceElementChecks = Valid.compose(initialTypeElementChecks(IXingYiResourceDefn.class), checkRootUrlStartsWithHost, checkRootUrlHasId);
    Valid<ElementFail, TypeElement> initialViewElementChecks = initialTypeElementChecks(IXingYiViewDefn.class);


    private List<TypeElement> getCheckedElements(Class<? extends Annotation> annotation, RoundEnvironment env, LoggerAdapter log, Valid<ElementFail, TypeElement> valid) {
        List<TypeElement> elements = Sets.sortedList((Set<TypeElement>) env.getElementsAnnotatedWith(annotation), comparator());
        List<ElementFail> elementFails = Valid.checkAll(elements, valid);
        Lists.foreach(elementFails, log::error);
        return elements;
    }

    private List<ResourceDom> makeResourceDomResults(RoundEnvironment env, LoggerAdapter log, ElementToBundle bundle, IViewDefnNameToViewName viewNamesMap) {
        List<TypeElement> elements = getCheckedElements(Resource.class, env, log, initialResourceElementChecks);
        return log.logFailuresAndReturnSuccesses(Lists.map(elements, e -> bundle.elementToEntityDom(bundle.elementToEntityNames().apply(e)).apply(e, viewNamesMap)));
    }

    private List<ViewDom> makeViewDoms(RoundEnvironment env, LoggerAdapter log, ElementToBundle bundle, List<ResourceDom> resourceDoms, IViewDefnNameToViewName viewNamesMap) {
        List<TypeElement> viewElements = getCheckedElements(View.class, env, log, initialViewElementChecks);
        return log.logFailuresAndReturnSuccesses(Lists.map(viewElements,
                v -> bundle.elementToViewNames().apply(v).flatMap(vn -> bundle.elementToViewDom(vn).apply(v,viewNamesMap, resourceDoms))));
    }
    Result<String, FileDefn> makeServer(ServerDom serverDom) {
        return new ServerFileMaker().apply(serverDom);
    }


    @Override
    //TODO Refactor
    public boolean process(Set<? extends TypeElement> annoations, RoundEnvironment env) {
        MonadDefn monadDefn = new CompletableFutureDefn();
        LoggerAdapter log = LoggerAdapter.fromMessager(messager);
        ElementToBundle bundle = ElementToBundle.simple(log);
        log.info("Processing XingYi Annotations");
        try {
            IViewDefnNameToViewName viewNamesMap = elementsToMapOfViewDefnToView.apply(Sets.sortedList((Set<TypeElement>) env.getElementsAnnotatedWith(View.class), comparator()));

            List<ResourceDom> resourceDoms = makeResourceDomResults(env, log, bundle, viewNamesMap);
            List<ViewDom> viewDoms = makeViewDoms(env, log, bundle, resourceDoms, viewNamesMap);
            CodeDom codeDom = new CodeDom(monadDefn, resourceDoms, viewDoms);

            log.info("Made codeDom: " + codeDom);
            List<ServerDom> serverDoms = log.logFailuresAndReturnSuccesses(Lists.map(Sets.toList(env.getElementsAnnotatedWith(Server.class)), e1 -> ServerDom.create(names, e1, codeDom)));

            List<FileDefn> codeContent = log.logFailuresAndReturnStringSuccesses(makeContent(log, codeDom));
            List<FileDefn> systemContent = log.logFailuresAndReturnStringSuccesses(Lists.map(serverDoms, sd -> makeServer(sd)));

            makeHttpService("one.xingyi.core.httpClient", monadDefn).forEach(x -> {
                try { makeClassFile(x);} catch (Exception e) {}
            });
            Lists.foreach(Lists.append(systemContent, codeContent), this::makeClassFile);
            validateLens(env, log, codeDom);
        } catch (
                Exception e) {
            Throwable unwrapped = WrappedException.unWrap(e);
            log.error("In Annotation Processor\n" + Strings.getFrom(unwrapped::printStackTrace));
        }
        return false;
    }

    Result<String, FileDefn> makeHttpService(String packageName, MonadDefn monadDefn) { return new HttpServiceFileMaker(packageName).apply(monadDefn); }

    List<Result<String, FileDefn>> makeContent(LoggerAdapter log, CodeDom codeDom) {
        List<IFileMaker<ResourceDom>> entityFileMakes = Arrays.asList(
                new CodeDomDebugFileMaker(),
                new ServerInterfaceFileMaker(),
                new ServerResourceFileMaker(),
                new ClientResourceFileMaker(),
                new ServerCompanionFileMaker(),
                new ClientResourceCompanionFileMaker(codeDom.monadDefn),
                new ServerControllerFileMaker(codeDom.monadDefn));

        List<IFileMaker<ViewDomAndItsResourceDom>> viewFileMakers = List.of(
                new ViewDomDebugFileMaker(),
                new ClientViewInterfaceFileMaker(codeDom.monadDefn),
                new ClientViewCompanionFileMaker(codeDom.monadDefn),
                new ClientViewImplFileMaker()
        );

        List<Result<String, FileDefn>> fromResources = Lists.flatMap(codeDom.resourceDoms, resourceDom -> Lists.map(entityFileMakes, f1 -> f1.apply(resourceDom)));
        List<Result<String, FileDefn>> fromViews = Lists.flatMap(codeDom.viewsAndDoms, viewDom -> Lists.map(viewFileMakers, f11 -> f11.apply(viewDom)));

//        Lists.foreach(Lists.append(Result.failures(Lists.flatMap(codeDom.resourceDoms, entityDom -> Lists.map(entityFileMakes, f -> f.apply(entityDom)))),
//                Result.failures(Lists.flatMap(codeDom.viewsAndDoms, viewDom -> Lists.map(viewFileMakers, f1 -> f1.apply(viewDom))))), log::error);
        return Lists.append(fromResources, fromViews);
    }

    void makeClassFile(FileDefn fileDefn) {
        WrappedException.wrap(() -> {
            JavaFileObject builderFile = filer.createSourceFile(fileDefn.packageAndClassName.asString());
//            messager.printMessage(Diagnostic.Kind.NOTE, "making  " + fileDefn.packageAndClassName + "->" + builderFile.toUri());
            Files.setText(() -> new PrintWriter(builderFile.openWriter()), fileDefn.content);
        });
    }

    @Override public SourceVersion getSupportedSourceVersion() { return SourceVersion.latestSupported(); }
    @Override public Set<String> getSupportedAnnotationTypes() {
        return Set.of(
                Resource.class.getName(),
                View.class.getName(),
                Server.class.getName(),
                ValidateManyLens.class.getName());
    }


    private void validateLens(RoundEnvironment env, LoggerAdapter log, CodeDom codeDom) throws IOException {
        Set<? extends Element> validate = env.getElementsAnnotatedWith(ValidateManyLens.class);
        for (Element v : validate) {
//                PackageAndClassName packageAndClassName = new PackageAndClassName(v.getAnnotation(ValidateLens.class).value());
            for (ValidateLens annotation : v.getAnnotation(ValidateManyLens.class).value()) {
                try {
                    FileObject fileObject = filer.getResource(StandardLocation.CLASS_PATH, "", annotation.value());
                    File outputFile = new File(fileObject.toUri().toURL().getFile());
                    File file = new File(outputFile.getParentFile().getParentFile().getParentFile(), "src/main/resources/" + annotation.value());
//                log.error(v, "trying" +file.getAbsolutePath());
//                File file = new File(v.getAnnotation(ValidateLens.class).value());
//                        log.info("Checking lens in " + file.getAbsolutePath() + " " + file.exists());
//                if (!file.exists())
//                    log.error(v, "Cannot find file " + file.getAbsolutePath());
                    String text = Files.getText(file);
//                log.error(v, "Found textt" + text);
                    Set<String> expectedLens = new HashSet<>(Lists.filter(Arrays.asList(text.split("\n")), l -> l.length() > 0));
                    Set<String> actualLens = new HashSet<>(Lists.flatMap(codeDom.resourceDoms, ed -> ed.fields.withDeprecatedmap(fd -> fd.lensName)));
                    actualLens.add("lens_EntityDetails_urlPattern");
                    Set<String> originalExpectedLens = new HashSet<>(expectedLens);
                    Set<String> originalActualsLens = new HashSet<>(actualLens);
                    expectedLens.removeAll(originalActualsLens);
                    if (expectedLens.size() > 0) {
                        String msg = "(" + annotation.value() + ") Sometimes this is caused by incremental compilation\nMissing lens " + expectedLens + "\nActual lens are" + "\n" + Sets.sortedString(originalActualsLens, ", ");
                        if (annotation.error())
                            log.error(v, msg);
                        else
                            log.warning(v, msg);
                    }
                    if (annotation.exact()) {
                        actualLens.removeAll(originalExpectedLens);
                        if (actualLens.size() > 0) {
                            String msg = "(" + annotation.value() + ") There are lens supported " + actualLens + " that aren't in the validation file\nActual lens are" + "\n" + Sets.sortedString(originalActualsLens, ", ");
                            if (annotation.error())
                                log.error(v, msg);
                            else
                                log.warning(v, msg);
                        }
                    }
                } catch (FileNotFoundException ex) {
                    log.error(v, "Cannot find file for " + annotation.value() + ". It should be in the root class path. For example in src/main/resources ");
                }
            }
        }
    }

}