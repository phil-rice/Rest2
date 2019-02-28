package one.xingyi.core.utils;
public interface BiFunctionWithException<T1, T2, Res> {
    Res apply(T1 t1, T2 t2) throws Exception;
}
