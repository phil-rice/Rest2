package one.xingyi.core.annotationProcessors;

import one.xingyi.core.annotations.Entity;
import one.xingyi.core.annotations.View;
import one.xingyi.core.codeDom.CodeDom;
import one.xingyi.core.codeDom.EntityDom;
import one.xingyi.core.codeDom.ViewDom;
import one.xingyi.core.filemaker.*;
import one.xingyi.core.names.EntityNames;
import one.xingyi.core.names.IClassNameStrategy;
import one.xingyi.core.names.IPackageNameStrategy;
import one.xingyi.core.names.IServerNames;
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
            log.info("Made entityDoms: " + entityDomResults);

            Result.merge(entityDomResults).result().ifPresent(entityDoms -> {
                List<ViewDom> viewDoms = Lists.map(Sets.sortedList(env.getElementsAnnotatedWith(View.class), comparator()), v -> bundle.elementToViewDom().apply(entityDoms, v));
                CodeDom codeDom = new CodeDom(entityDoms, viewDoms);
                List<FileDefn> content = makeContent(codeDom);
                log.info("Started");
                for (FileDefn fileDefn : content)
                    makeClassFile(fileDefn);

            });
        } catch (Exception e) {
            log.error("In Annotation Processor\n" + e.getClass().getName() + "\n" + Lists.mapJoin(Arrays.asList(e.getStackTrace()), "\n", Objects::toString));
        }
        return false;
    }

    List<FileDefn> makeContent(CodeDom codeDom) {
        List<IFileMaker<EntityDom>> entityFile = Arrays.asList(
                new CodeDomDebugFileMaker(),
                new ServerInterfaceFileMaker(),
                new ServerEntityFileMaker());
        return Lists.flatMap(codeDom.entityDoms, entityDom -> Lists.map(entityFile, f -> f.apply(entityDom)));
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