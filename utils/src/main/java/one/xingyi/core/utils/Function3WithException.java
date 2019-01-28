package one.xingyi.core.utils;
public interface Function3WithException<T1, T2, T3, Res> {
    Res apply(T1 t1, T2 t2, T3 t3) throws Exception;
}
