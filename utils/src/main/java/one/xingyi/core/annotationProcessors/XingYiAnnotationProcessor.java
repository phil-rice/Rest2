package one.xingyi.core.annotationProcessors;

import one.xingyi.core.annotations.Entity;
import one.xingyi.core.annotations.View;
import one.xingyi.core.codeDom.CodeDom;
import one.xingyi.core.codeDom.EntityDom;
import one.xingyi.core.codeDom.ViewDom;
import one.xingyi.core.filemaker.*;
import one.xingyi.core.names.*;
import one.xingyi.core.utils.*;
import one.xingyi.core.validation.Result;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.JavaFileObject;
import java.io.PrintWriter;
import java.util.*;
public class XingYiAnnotationProcessor extends AbstractProcessor {
    final IServerNames names = IServerNames.simple(IPackageNameStrategy.simple, IClassNameStrategy.simple);
    ElementToBundle bundle = ElementToBundle.simple;

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

    @Override
    public boolean process(Set<? extends TypeElement> annoations, RoundEnvironment env) {
        LoggerAdapter log = LoggerAdapter.fromMessager(messager);
        try {
            Set<? extends Element> elements = env.getElementsAnnotatedWith(Entity.class);
            log.info("Found these entities: " + elements);
            List<Result<ElementFail, EntityDom>> entityDomResults = Lists.map(
                    Sets.sortedList(elements, comparator()),
                    e -> bundle.elementToEntityNames().apply(e).flatMap(entityNames -> bundle.elementToEntityDom(entityNames).apply((TypeElement) e)));


            Result<ElementFail, List<EntityDom>> entityDoms = Result.merge(entityDomResults);

            List<? extends Element> viewElements = Sets.sortedList(env.getElementsAnnotatedWith(View.class), comparator());
            log.info("Making viewDoms elements: " + viewElements);
            List<Result<ElementFail, ViewDom>> viewDomResults = Lists.map(viewElements,
                    v -> bundle.elementToViewNames().apply((TypeElement) v).flatMap(vn ->
                            bundle.elementToViewDom(vn).apply((TypeElement) v)
                    )
            );
            Result<ElementFail, List<ViewDom>> viewDoms = Result.merge(viewDomResults);
            log.info("Made viewDoms: " + viewDoms);

            Result.join(entityDoms, viewDoms, (ed, vd) -> new CodeDom(ed, vd)).forEach(codeDom -> {
                List<FileDefn> content = makeContent(codeDom);
                log.info("Started");
                for (FileDefn fileDefn : content)
                    makeClassFile(fileDefn);
            });

        } catch (Exception e) {
            log.error("In Annotation Processor\n" + Strings.getFrom(e::printStackTrace));
        }
        return false;
    }

    List<FileDefn> makeContent(CodeDom codeDom) {
        List<IFileMaker<EntityDom>> entityFileMakes = Arrays.asList(
                new CodeDomDebugFileMaker(),
                new ServerInterfaceFileMaker(),
                new ServerEntityFileMaker(),
                new ServerCompanionFileMaker());
        List<FileDefn> fromCodeDom = Lists.flatMap(codeDom.entityDoms, entityDom -> Lists.map(entityFileMakes, f -> f.apply(entityDom)));

        List<IFileMaker<ViewDom>> viewFileMakers = List.of(
                new ViewDomDebugFileMaker()
        );
        List<FileDefn> fromViewDom = Lists.flatMap(codeDom.viewDoms, viewDom -> Lists.map(viewFileMakers, f -> f.apply(viewDom)));

        return Lists.<FileDefn>append(fromCodeDom, fromViewDom);
    }

    void makeClassFile(FileDefn fileDefn) {
        WrappedException.wrap(() -> {
            JavaFileObject builderFile = filer.createSourceFile(fileDefn.packageAndClassName.asString());
//            messager.printMessage(Diagnostic.Kind.NOTE, "making  " + clientImpl + "->" + builderFile.toUri());
            Files.setText(() -> new PrintWriter(builderFile.openWriter()), fileDefn.content);
        });
    }

    @Override public SourceVersion getSupportedSourceVersion() { return SourceVersion.latestSupported(); }
    @Override public Set<String> getSupportedAnnotationTypes() {return Set.of(Entity.class.getName(), View.class.getName()); }
}