package org.invertthepyramid.involved.utilities;

import com.twitter.util.Future;
import org.invertthepyramid.involved.mdm.Function;
import org.invertthepyramid.involved.mdm.MDMServiceException;
import scala.runtime.AbstractFunction1;

public interface Wrap {
    static <T> Getter<T> wrap(IErrorStrategy strategy, Getter<Future<T>> getter) {
        return new WrapImpl<>(strategy, getter);
    }

    static <T, T1> Getter<Future<T1>> map(Future<T> f, java.util.function.Function<T, T1> fn) {
        return new MapImpl<>(f, fn);
    }
}

