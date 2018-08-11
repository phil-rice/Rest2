package org.invertthepyramid.involved;

import com.twitter.util.Future;
import org.invertthepyramid.involved.misc.Function;
import org.invertthepyramid.involved.misc.MDMServiceException;
import scala.Function1;
import scala.runtime.AbstractFunction1;

public interface Wrap {
    static <T> T wrap(IErrorStrategy strategy, Getter<Future<T>> getter) {
        try {
            return Function.destroyMyPerformance(getter.get());
        } catch (MDMServiceException e) {throw strategy.handleMdmException(e);}
    }

    static <T, T1> Getter<Future<T1>> map(Future<T> f, java.util.function.Function<T, T1> fn) {
        return () -> f.map(new AbstractFunction1<T, T1>() {
            @Override
            public T1 apply(T v1) {
                return fn.apply(v1);
            }
        });
    }
}

