package one.xingyi.core.utils;
public interface BiConsumerWithException<T1,T2> {
    void accept(T1 t1, T2 t2) throws Exception;
}
