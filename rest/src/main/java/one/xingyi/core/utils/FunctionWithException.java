package one.xingyi.core.utils;
public interface FunctionWithException<From,To> {
    To apply(From from) throws Throwable;
}
