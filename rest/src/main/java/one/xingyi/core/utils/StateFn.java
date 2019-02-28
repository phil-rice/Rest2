
package one.xingyi.core.utils;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode

/** The intent of this class to to be passed to the httpservice 'postWithState'. After getting an object, if the requested method is allowed
 * the correct state function is called. The first parameter is 'the existing value' the second is the actual post.
 * Both return a T1. This allows for either throwing exceptions, or returning Either or really whatever the caller wants
 */
public class StateFn<T, T1> {
    public final Function<T, T1> wrongState;
    public final BiFunction<T, Function<T, CompletableFuture<T>>, T1> correctState;
}