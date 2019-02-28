package one.xingyi.core.validation;
import one.xingyi.core.utils.Lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
public interface Valid<Fail, T> extends Function<T, List<Fail>> {
    static <Fail, T> Valid<Fail, T> check(Function<T, Boolean> isOk, Function<T, Fail> message) { return t -> isOk.apply(t) ? List.of() : List.of(message.apply(t)); }
    static <Fail, Fail1, T> Valid<Fail1, T> check(Function<T, Boolean> isOk, Function<T, Fail> message, BiFunction<T, Fail, Fail1> failFn) { return t -> isOk.apply(t) ? List.of() : List.of(failFn.apply(t, message.apply(t))); }


    static <Fail, T, T1> Valid<Fail, T> compose(Function<T, T1> fn, Valid<Fail, T1>... valids) {
        return (T t) -> {
            T1 t1 = fn.apply(t);
            List<Fail> fails = Lists.<Valid<Fail, T1>, Fail>flatMap(Arrays.asList(valids), v -> v.apply(t1));
            return fails;
        };
    }
    static <Fail, T> List<Fail> checkAll(List<T> ts, Valid<Fail, T> valid) { return Lists.flatMap(ts, s -> valid.apply(s)); }
}
