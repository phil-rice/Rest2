package org.invertthepyramid.involved.utilities;

import com.twitter.util.Future;
import org.invertthepyramid.involved.mdm.Function;
import org.invertthepyramid.involved.mdm.MDMServiceException;

public class WrapImpl<T> implements Getter<T> {
    public final IErrorStrategy strategy;
    public final Getter<Future<T>> getter;

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
