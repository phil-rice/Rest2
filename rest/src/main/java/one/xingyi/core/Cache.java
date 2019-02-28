package one.xingyi.core;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
public interface Cache<From, To> extends Function<From, To> {
    static <From, To> Cache<From, To> dumbCache(Function<From, To> rawFn) { return new DumbCache<>(rawFn);}
    void invalidate(From from);
}

@RequiredArgsConstructor
@ToString
class DumbCache<From, To> implements Cache<From, To> {
    final Function<From, To> rawFn;
    final Map<From, To> map = Collections.synchronizedMap(new HashMap<>());
    @Override public To apply(From from) {
        return map.computeIfAbsent(from, rawFn);
    }
    @Override public void invalidate(From from) {
        map.remove(from);
    }
}
