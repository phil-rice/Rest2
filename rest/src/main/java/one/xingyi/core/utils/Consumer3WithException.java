package one.xingyi.core.utils;
public interface Consumer3WithException<T1,T2,T3> {
    void accept(T1 t1, T2 t2, T3 t3) throws Exception;
}
