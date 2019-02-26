package one.xingyi.core.utils;
import lombok.RequiredArgsConstructor;
import one.xingyi.core.validation.Result;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
public interface PartialFunction<From, To> extends Function<From, To> {
    boolean isDefinedAt(From from);
    default To orDefault(From from, Supplier<To> defSupplier) {
        if (isDefinedAt(from)) return apply(from);
        else return defSupplier.get();
    }
    public static <From, To> PartialFunction<From, To> pf(Function<From, Boolean> acceptor, Function<From, To> fn) {
        return new SimplePartialFunction<>(acceptor, fn);
    }
    @SafeVarargs public static <From, To> PartialFunction<From, To> chain(PartialFunction<From, To>... pfs) {
        return new ChainPartialFunction(Arrays.asList(pfs));
    }

}
@RequiredArgsConstructor
class ChainPartialFunction<From, To> implements PartialFunction<From, To> {
    final List<PartialFunction<From, To>> partialFunctions;
    @Override public boolean isDefinedAt(From from) {
        for (PartialFunction<From, To> pf : partialFunctions)
            if (pf.isDefinedAt(from)) return true;
        return false;
    }
    @Override public To apply(From from) {
        for (PartialFunction<From, To> pf : partialFunctions)
            if (pf.isDefinedAt(from)) return pf.apply(from);
        throw new IllegalArgumentException("From is not defined: " + from);
    }
}
@RequiredArgsConstructor
class SimplePartialFunction<From, To> implements PartialFunction<From, To> {
    final Function<From, Boolean> acceptor;
    final Function<From, To> fn;
    @Override public boolean isDefinedAt(From from) { return acceptor.apply(from); }
    @Override public To apply(From from) { return fn.apply(from);}
}
