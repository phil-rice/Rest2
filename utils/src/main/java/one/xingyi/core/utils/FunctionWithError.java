package one.xingyi.core.utils;
public interface FunctionWithError<From,To> {
    To apply(From from) throws Exception;
}
