package one.xingyi.core.utils;
public interface ConsumerWithException<T> {
    void accept(T t) throws Exception;
}
