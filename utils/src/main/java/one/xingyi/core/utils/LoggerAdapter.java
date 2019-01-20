package one.xingyi.core.utils;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;
public interface LoggerAdapter {
    void info(String message);
    void warning(Element element, String message);
    void error(Element element, String message);
    void error(String message);
    LoggerAdapter withElement(Element element);
    static LoggerAdapter fromMessager(Messager messager, Element element) {
        return new LoggerAdapter() {
            @Override public void info(String message) { messager.printMessage(Diagnostic.Kind.NOTE, message, element); }
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
            @Override public void warning(Element element, String message) { messager.printMessage(Diagnostic.Kind.WARNING, message); }
            @Override public void error(Element element, String message) { messager.printMessage(Diagnostic.Kind.ERROR, message, element); }
            @Override public void error(String message) { messager.printMessage(Diagnostic.Kind.ERROR, message); }
            @Override public LoggerAdapter withElement(Element element) { return fromMessager(messager, element); }
        };
    }
}
