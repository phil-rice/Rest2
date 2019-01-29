package one.xingyi.core.utils;
public interface SupplierWithException<T> {
    T get() throws Exception;
}
