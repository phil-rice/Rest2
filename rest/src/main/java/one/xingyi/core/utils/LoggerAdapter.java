package one.xingyi.core.utils;
import one.xingyi.core.annotationProcessors.ElementFail;
import one.xingyi.core.validation.Result;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;
import java.util.List;
public interface LoggerAdapter {
    void info(String message);
    void info(Element element, String message);
    void warning(Element element, String message);
    void error(Element element, String message);
    default void error(ElementFail fail){ Optionals.doit(fail.optElement, ()-> error(fail.message), e-> error(e, fail.message)); }

    default <T> List<T> logFailuresAndReturnSuccesses(List<Result<ElementFail, T>> list) {
         Lists.foreach(Result.failures(list), this::error);
         return Result.successes(list);
    }
    default <T> List<T> logFailuresAndReturnStringSuccesses(List<Result<String, T>> list) {
         Lists.foreach(Result.failures(list), this::error);
         return Result.successes(list);
    }
    void error(String message);
    LoggerAdapter withElement(Element element);
    static LoggerAdapter fromMessager(Messager messager, Element element) {
        return new LoggerAdapter() {
            @Override public void info(String message) { messager.printMessage(Diagnostic.Kind.NOTE, message, element); }
            @Override public void info(Element element, String message) {messager.printMessage(Diagnostic.Kind.NOTE, message, element);}
            @Override public void warning(Element element, String message) { messager.printMessage(Diagnostic.Kind.WARNING, message, element); }
            @Override public void error(Element element, String message) { messager.printMessage(Diagnostic.Kind.ERROR, message, element); }
            @Override public void error(String message) { messager.printMessage(Diagnostic.Kind.ERROR, message, element); }
            @Override public LoggerAdapter withElement(Element element) { return fromMessager(messager, element); }
        };
    }
    static LoggerAdapter fromMessager(Messager messager) {
        return new LoggerAdapter() {
            @Override public void info(String message) {
                messager.printMessage(Diagnostic.Kind.NOTE, message);
            }
            @Override public void info(Element element, String message) {messager.printMessage(Diagnostic.Kind.NOTE, message, element); }
            @Override public void warning(Element element, String message) { messager.printMessage(Diagnostic.Kind.WARNING, message, element); }
            @Override public void error(Element element, String message) { messager.printMessage(Diagnostic.Kind.ERROR, message, element); }
            @Override public void error(String message) { messager.printMessage(Diagnostic.Kind.ERROR, message); }
            @Override public LoggerAdapter withElement(Element element) { return fromMessager(messager, element); }
        };
    }
}
