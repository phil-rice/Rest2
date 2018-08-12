package org.invertthepyramid.involved;

import org.invertthepyramid.involved.utilities.Getter;
import org.junit.Assert;

import java.util.function.Function;

public class FunctionsFixture {

    public static <T> Getter<T> constantGetter(T t){
        return () -> t;
    }
    public static <From, To> Function<From, To> constant(From expectedFrom, To resultingTo) {
        return (from) -> {
            Assert.assertEquals(expectedFrom, from);
            return resultingTo;
        };
    }
}
