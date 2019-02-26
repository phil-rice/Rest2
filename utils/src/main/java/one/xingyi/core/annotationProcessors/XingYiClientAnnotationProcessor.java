package one.xingyi.core.annotationProcessors;

import lombok.RequiredArgsConstructor;
import lombok.val;
import one.xingyi.core.annotations.*;
import one.xingyi.core.codeDom.*;
import one.xingyi.core.filemaker.*;
import one.xingyi.core.monad.CompletableFutureDefn;
import one.xingyi.core.monad.MonadDefn;
import one.xingyi.core.names.IClassNameStrategy;
import one.xingyi.core.names.IPackageNameStrategy;
import one.xingyi.core.names.IServerNames;
import one.xingyi.core.utils.*;
import one.xingyi.core.validation.Result;
import one.xingyi.core.validation.ResultAndFailures;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

@RequiredArgsConstructor
public class XingYiClientAnnotationProcessor extends AbstractProcessor {

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
    //TODO Refactor
    public boolean process(Set<? extends TypeElement> annoations, RoundEnvironment env) {
        IPackageNameStrategy packageNameStrategy = IPackageNameStrategy.simple;
        IClassNameStrategy classNameStrategy = IClassNameStrategy.simple;

        LoggerAdapter log = LoggerAdapter.fromMessager(messager);
        log.info("Processing XingYi Client Annotations");
        try {
            List<Element> elements = new ArrayList<>(env.getElementsAnnotatedWith(CombinedView.class));
            for (Element element : elements) {
                PackageAndClassName originaldefn = new PackageAndClassName(element.asType().toString());
                Result<String, String> result = classNameStrategy.toRoot(element.asType().toString(), originaldefn.className);
                if (result.fails().size() > 0)
                    log.error(element, result.fails().toString());
                Result<ElementFail, CompositeViewDom> resultDom = CompositeViewDom.create(log, (TypeElement) element, packageNameStrategy, classNameStrategy);
                for (ElementFail fail : resultDom.fails())
                    fail.logMe(log);
//                log.error(resultDom.toString());
                resultDom.forEach(dom -> {
                    for (IFileMaker<CompositeViewDom> maker : List.of(
                            new CompositeViewInterfaceMaker(),
                            new CompositeViewImplMaker(),
                            new CompositeViewCompanionMaker())) {
                        Result<String, FileDefn> makeFileResult = maker.apply(dom);
                        makeFileResult.forEach(defn -> makeClassFile(defn));
                        if (makeFileResult.fails().size() > 0)
                            log.error(element, makeFileResult.fails().toString());
                    } ;
                });
            }
        } catch (
                Exception e) {
            Throwable unwrapped = WrappedException.unWrap(e);
            log.error("In Client Annotation Processor\n" + Strings.getFrom(unwrapped::printStackTrace));
        }
        return false;
    }

    void makeClassFile(FileDefn fileDefn) {
        WrappedException.wrap(() -> {
            JavaFileObject builderFile = filer.createSourceFile(fileDefn.packageAndClassName.asString());
            Files.setText(() -> new PrintWriter(builderFile.openWriter()), fileDefn.content);
        });
    }

    @Override public SourceVersion getSupportedSourceVersion() { return SourceVersion.latestSupported(); }
    @Override public Set<String> getSupportedAnnotationTypes() {
        return Set.of(CombinedView.class.getName());
    }
}