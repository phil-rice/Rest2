package one.xingyi.core.utils;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import static junit.framework.TestCase.assertEquals;
public interface FunctionFixture {

    default public <From, To> Function<From, To> fn(From expected, To to) {
        return from -> {
            assertEquals(expected, from);
            return to;
        };
    }
    default public <From, To> Function<From, CompletableFuture<To>> kleisli(From expected, To to) {
        return from -> {
            assertEquals(expected, from);
            return CompletableFuture.completedFuture(to);
        };
    }
}
