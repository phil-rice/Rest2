package one.xingyi.core.annotationProcessors;
import lombok.RequiredArgsConstructor;
import one.xingyi.core.codeDom.PackageAndClassName;
import one.xingyi.core.utils.Files;
import one.xingyi.core.utils.LoggerAdapter;
import one.xingyi.core.utils.WrappedException;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.util.Set;
@RequiredArgsConstructor
abstract class ProcessAnnotations<T extends Annotation> {
    final Class<T> annotationClass;
    final RoundEnvironment env;
    final Messager messager;
    final Filer filer;
    abstract void doit(LoggerAdapter adapter, TypeElement element, T annotation);

    boolean validateElements(LoggerAdapter log,Set<? extends Element> elements) {return true;}
    public void process() {
        Set<? extends Element> elements = env.getElementsAnnotatedWith(annotationClass);
        if (validateElements(LoggerAdapter.fromMessager(messager),elements))
            for (Element element : elements) {
                if (element.getKind() == ElementKind.INTERFACE) {
                    LoggerAdapter log = LoggerAdapter.fromMessager(messager, element);
                    T annotation = element.getAnnotation(annotationClass);
                    doit(log, (TypeElement) element, annotation);
                }
            }
    }

    void makeClassFile(PackageAndClassName packageAndClassName, String classString, Element element) {
        WrappedException.wrap(() -> {
            JavaFileObject builderFile = filer.createSourceFile(packageAndClassName.asString());
//            messager.printMessage(Diagnostic.Kind.NOTE, "making  " + clientImpl + "->" + builderFile.toUri());
            Files.setText(() -> new PrintWriter(builderFile.openWriter()), classString);
        });
    }


}
