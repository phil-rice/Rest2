package org.invertthepyramid.involved;

import org.junit.Assert;

import java.util.function.Function;

public class FunctionsFixture {
    public static <From, To> Function<From, To> constant(From expectedFrom, To resultingTo) {
        return (from) -> {
            Assert.assertEquals(expectedFrom, from);
            return resultingTo;
        };
    }
}
