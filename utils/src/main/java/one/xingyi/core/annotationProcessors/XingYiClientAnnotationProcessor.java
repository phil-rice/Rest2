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

    @Override
    //TODO Refactor
    public boolean process(Set<? extends TypeElement> annoations, RoundEnvironment env) {
        LoggerAdapter log = LoggerAdapter.fromMessager(messager);
        ElementToBundle bundle = ElementToBundle.simple(log);
        log.info("Processing XingYi Client Annotations");
        try {
            log.info("Class: " + Class.forName(CombinedView.class.getName()));
            log.info("Class: " + Class.forName("one.xingyi.reference1.person.IPersonLine12ViewDefn"));
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