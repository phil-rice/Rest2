package one.xingyi.core.annotationProcessors;

import lombok.RequiredArgsConstructor;
import one.xingyi.core.annotations.*;
import one.xingyi.core.codeDom.*;
import one.xingyi.core.filemaker.*;
import one.xingyi.core.monad.CompletableFutureDefn;
import one.xingyi.core.monad.MonadDefn;
import one.xingyi.core.names.*;
import one.xingyi.core.sdk.IXingYiResource;
import one.xingyi.core.utils.*;
import one.xingyi.core.validation.Result;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.function.Function;

import lombok.val;
import one.xingyi.core.validation.ResultAndFailures;
import one.xingyi.core.validation.Valid;

@RequiredArgsConstructor
public class XingYiAnnotationProcessor extends AbstractProcessor {
    final IServerNames names = IServerNames.simple(IPackageNameStrategy.simple, IClassNameStrategy.simple);

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

    Valid<String, Element> checkRootUrlStartsWithHost = Valid.check(e -> {
        String rootUrl = e.getAnnotation(Resource.class).rootUrl();
        return rootUrl.length() > 0 && rootUrl.startsWith("{host}");
    }, e -> "Root Url needs to start with {host}");

    Valid<ElementFail, TypeElement> check(Function<TypeElement, Boolean> checkFn, Function<TypeElement, String> message) {return Valid.<String, ElementFail, TypeElement>check(checkFn, message, (e, s) -> new ElementFail(s, e));}

    Valid<ElementFail, TypeElement> checkElementIsAnInterface = check(e -> e.getKind() == ElementKind.INTERFACE, e -> "Must be an interface");
    Valid<ElementFail, TypeElement> checkNameStartsWithI = check(e -> e.getSimpleName().toString().startsWith("I"), e -> "Name must start with an I");
    Valid<ElementFail, TypeElement> checkNameEndsWithDefn = check(e -> e.getSimpleName().toString().endsWith("Defn"), e -> "Name must end with Defn");
    Valid<ElementFail, TypeElement> checkImplements(String className) {return check(e -> Lists.find(e.getInterfaces(), i -> i.toString().startsWith(className)).isPresent(), e -> "Must extend " + className);}
    Valid<ElementFail, Element> initialTypeElementChecks = Valid.compose(e -> (TypeElement) e,
            checkElementIsAnInterface, checkNameStartsWithI, checkNameEndsWithDefn, checkImplements(IXingYiResource.class.getName()));


    private List<Result<ElementFail, ResourceDom>> makeResourceDomResults(RoundEnvironment env, LoggerAdapter log, ElementToBundle bundle) {
        List<Element> elements = Sets.sortedList((Set<Element>) env.getElementsAnnotatedWith(Resource.class), comparator());
        List<ElementFail> elementFails = Valid.checkAll(elements, initialTypeElementChecks);
        Lists.foreach(elementFails, log::error);
        return Lists.map(elements, e -> bundle.elementToEntityDom(bundle.elementToEntityNames().apply(e)).apply((TypeElement) e));
    }


    @Override
    //TODO Refactor
    public boolean process(Set<? extends TypeElement> annoations, RoundEnvironment env) {
        MonadDefn monadDefn = new CompletableFutureDefn();
        LoggerAdapter log = LoggerAdapter.fromMessager(messager);
        ElementToBundle bundle = ElementToBundle.simple(log);
        log.info("Processing XingYi Annotations");
        try {
            List<Result<ElementFail, ResourceDom>> resourceDomsResults = makeResourceDomResults(env, log, bundle);
            List<ResourceDom> resourceDoms = Result.successes(resourceDomsResults);
            log.info("Made entityDoms: " + resourceDoms);


            List<? extends Element> viewElements = Sets.sortedList(env.getElementsAnnotatedWith(View.class), comparator());
            log.info("Making viewDoms elements: " + viewElements);
            List<Result<ElementFail, ViewDom>> viewDomResults = Lists.map(viewElements,
                    v -> bundle.elementToViewNames().apply((TypeElement) v).flatMap(vn ->
                            bundle.elementToViewDom(vn).apply((TypeElement) v)
                    )
            );
            val viewDoms = Result.successes(viewDomResults);
            log.info("Made viewDoms: " + Lists.map(viewDoms, vd-> vd.viewNames.clientView.asString()));

            //TODO Work out how to spot at this stage or before if there are classes in the names of fields in views. Best done when the element is available

            CodeDom codeDom = new CodeDom(monadDefn, resourceDoms, viewDoms);
            log.info("Made codeDom: " + codeDom);

            ResultAndFailures<String, List<FileDefn>> codeContentAndIssues = makeContent(log,codeDom);
            for (String issue : codeContentAndIssues.failures) {
                log.error(issue);
            }
            List<FileDefn> codeContent = codeContentAndIssues.t;
            val serverElements = Sets.toList(env.getElementsAnnotatedWith(Server.class));
//            val getElements = Sets.toList((Set<TypeElement>) env.getElementsAnnotatedWith(Get.class));

            List<Result<ElementFail, ServerDom>> serverDomResults = Lists.map(serverElements, e -> ServerDom.create(names, e, codeDom));
            List<ServerDom> serverDoms = Result.successes(serverDomResults);
            List<Result<String, FileDefn>> systemContentResult = Lists.map(serverDoms, sd -> makeServer(sd));
            for (String issue : Result.failures(systemContentResult))
                log.error(issue);
            Result<String, FileDefn> t = makeHttpService("one.xingyi.core.httpClient", monadDefn);
            t.forEach(x -> {try { makeClassFile(x);} catch (Exception e) {}});
            List<FileDefn> systemContent = Result.successes(systemContentResult);
            log.info("Found the following system contents" + Lists.mapJoin(systemContent, ",", s -> s.packageAndClassName.asString()));

            for (FileDefn fileDefn : Lists.append(systemContent, codeContent)) {
//                log.info("making scode or system content "+fileDefn.packageAndClassName.className);
                makeClassFile(fileDefn);
//                log.info("   ... finished "+fileDefn.packageAndClassName.className);
            }
            log.info("got to end of creation");
            for (ElementFail fail : Lists.append(Result.failures(resourceDomsResults), Result.failures(viewDomResults), Result.failures(serverDomResults)))
                Optionals.doit(fail.optElement, () -> log.error(fail.message + "no element"), e -> log.error(e, fail.message + " element " + e));
            validateLens(env, log, codeDom);

        } catch (
                Exception e) {
            Throwable unwrapped = WrappedException.unWrap(e);
            log.error("In Annotation Processor\n" + Strings.getFrom(unwrapped::printStackTrace));
        }
        return false;
    }
    Result<String, FileDefn> makeServer(ServerDom serverDom) {
        return new ServerFileMaker().apply(serverDom);
    }

    Result<String, FileDefn> makeHttpService(String packageName, MonadDefn monadDefn) {
        return new HttpServiceFileMaker(packageName).apply(monadDefn);
    }

    ResultAndFailures<String, List<FileDefn>> makeContent(LoggerAdapter log, CodeDom codeDom) {
        List<IFileMaker<ResourceDom>> entityFileMakes = Arrays.asList(
                new CodeDomDebugFileMaker(),
                new ServerInterfaceFileMaker(),
                new ServerResourceFileMaker(),
                new ClientResourceFileMaker(),
                new ServerCompanionFileMaker(),
                new ClientResourceCompanionFileMaker(codeDom.monadDefn),
                new ServerControllerFileMaker(codeDom.monadDefn));
        List<Result<String, FileDefn>> fromCodeDomResults = Lists.flatMap(codeDom.resourceDoms, entityDom -> Lists.map(entityFileMakes, f -> f.apply(entityDom)));
        List<FileDefn> fromCodeDom = Result.successes(fromCodeDomResults);
        List<String> fromCodeDomIssues = Result.failures(fromCodeDomResults);

        log.info("The viewDoms are " + codeDom.viewsAndDoms.size());
        List<IFileMaker<ViewDomAndItsResourceDom>> viewFileMakers = List.of(
                new ViewDomDebugFileMaker(),
                new ClientViewInterfaceFileMaker(codeDom.monadDefn),
                new ClientViewCompanionFileMaker(codeDom.monadDefn),
                new ClientViewImplFileMaker()
        );

        List<Result<String, FileDefn>> fromViewDomResults = Lists.flatMap(codeDom.viewsAndDoms, viewDom -> Lists.map(viewFileMakers, f -> f.apply(viewDom)));
        log.info("The viewDoms results " + fromViewDomResults);

        List<String> fromViewDomIssues = Result.failures(fromViewDomResults);
        List<FileDefn> fromViewDom = Result.successes(fromViewDomResults);


        return new ResultAndFailures<>(Lists.append(fromCodeDomIssues, fromViewDomIssues), Lists.<FileDefn>append(fromCodeDom, fromViewDom));
    }

    void makeClassFile(FileDefn fileDefn) {
        WrappedException.wrap(() -> {
            JavaFileObject builderFile = filer.createSourceFile(fileDefn.packageAndClassName.asString());
            messager.printMessage(Diagnostic.Kind.NOTE, "making  " + fileDefn.packageAndClassName + "->" + builderFile.toUri());
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