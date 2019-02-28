package one.xingyi.core.utils;
public interface CallableWithThrowable<T> {
    T call() throws Throwable;
}
