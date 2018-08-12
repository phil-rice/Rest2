package org.invertthepyramid.involved;

import com.twitter.util.Future;
import org.invertthepyramid.involved.misc.Function;
import org.invertthepyramid.involved.misc.MDMServiceException;
import scala.runtime.AbstractFunction1;

public interface Wrap {
    static <T> Getter<T> wrap(IErrorStrategy strategy, Getter<Future<T>> getter) {
        return new WrapImpl<>(strategy, getter);
    }

    static <T, T1> Getter<Future<T1>> map(Future<T> f, java.util.function.Function<T, T1> fn) {
        return new MapImpl<>(f, fn);
    }
}

class WrapImpl<T> implements Getter<T> {
    IErrorStrategy strategy;
    Getter<Future<T>> getter;

    public WrapImpl(IErrorStrategy strategy, Getter<Future<T>> getter) {
        this.strategy = strategy;
        this.getter = getter;
    }

    @Override
    public T get() {
        try {
            return Function.destroyMyPerformance(getter.get());
        } catch (MDMServiceException e) {throw strategy.handleMdmException(e);}
    }
}

class MapImpl<T, T1> implements Getter<Future<T1>> {
    Future<T> f;
    java.util.function.Function<T, T1> fn;

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
