package org.invertthepyramid.involved.utilities;

import com.twitter.util.Future;
import scala.runtime.AbstractFunction1;

public class MapImpl<T, T1> implements Getter<Future<T1>> {
    public final Future<T> f;
    public final java.util.function.Function<T, T1> fn;

    public MapImpl(Future<T> f, java.util.function.Function<T, T1> fn) {
        this.f = f;
        this.fn = fn;
    }

    @Override
    public Future<T1> get() {
        return f.map(new AbstractFunction1<T, T1>() {
            @Override
            public T1 apply(T v1) { return fn.apply(v1); }
        });
    }
}
