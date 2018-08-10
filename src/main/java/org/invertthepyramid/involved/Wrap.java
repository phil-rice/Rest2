package org.invertthepyramid.involved;

import com.twitter.util.Future;
import org.invertthepyramid.involved.misc.Function;
import org.invertthepyramid.involved.misc.MDMServiceException;

public interface Wrap {
  static <T> T wrap(IErrorStrategy strategy, Getter<Future<T>> getter) {
        try {
            return Function.destroyMyPerformance(getter.get());
        } catch (MDMServiceException e) {throw strategy.handleMdmException(e);}
    }
}
