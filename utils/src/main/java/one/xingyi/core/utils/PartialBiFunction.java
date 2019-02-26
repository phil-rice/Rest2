package one.xingyi.core.utils;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
public interface PartialBiFunction<From1, From2, To> extends BiFunction<From1, From2, To> {
    boolean isDefinedAt(From1 from1, From2 from2);
    default To orDefault(From1 from1, From2 from2, Supplier<To> defSupplier) {
        if (isDefinedAt(from1, from2)) return apply(from1, from2);
        else return defSupplier.get();
    }
    public static <From1, From2, To> PartialBiFunction<From1, From2, To> pf(BiFunction<From1, From2, Boolean> acceptor, BiFunction<From1, From2, To> fn) {
        return new SimplePartialBiFunction<>(acceptor, fn);
    }
    @SafeVarargs public static <From1, From2, To> PartialBiFunction<From1, From2, To> chain(PartialBiFunction<From1, From2, To>... pfs) {
        return new ChainPartialBiFunction(Arrays.asList(pfs));
    }

}
@RequiredArgsConstructor
class ChainPartialBiFunction<From1, From2, To> implements PartialBiFunction<From1, From2, To> {
    final List<PartialBiFunction<From1, From2, To>> partialFunctions;
    @Override public boolean isDefinedAt(From1 from1, From2 from2) {
        for (PartialBiFunction<From1, From2, To> pf : partialFunctions)
            if (pf.isDefinedAt(from1, from2)) return true;
        return false;
    }
    @Override public To apply(From1 from1, From2 from2) {
        for (PartialBiFunction<From1, From2, To> pf : partialFunctions)
            if (pf.isDefinedAt(from1, from2)) return pf.apply(from1, from2);
        throw new IllegalArgumentException("From is not defined: " + from1 + ", " + from2);
    }
}
@RequiredArgsConstructor
class SimplePartialBiFunction<From1, From2, To> implements PartialBiFunction<From1, From2, To> {
    final BiFunction<From1, From2, Boolean> acceptor;
    final BiFunction<From1, From2, To> fn;
    @Override public boolean isDefinedAt(From1 from1, From2 from2) { return acceptor.apply(from1, from2); }
    @Override public To apply(From1 from1, From2 from2) { return fn.apply(from1, from2);}
}
