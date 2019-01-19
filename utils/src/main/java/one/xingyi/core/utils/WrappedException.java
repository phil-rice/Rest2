package one.xingyi.core.utils;

import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
public class WrappedException extends RuntimeException {

    public WrappedException(Throwable cause) {
        super(cause);
    }
    public static <T> T wrapCallable(Callable<T> callable) {
        try {
            return callable.call();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new WrappedException(e);
        }
    }
    public static <T> Consumer<T> wrapConsumer(ConsumerWithException<T> consumer) {
        return t -> {
            try {
                consumer.accept(t);
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw new WrappedException(e);
            }
        };
    }
    public static <T1, T2> Function<T1, T2> wrapFn(Function<T1, T2> fn) {
        return from -> {
            try {
                return fn.apply(from);
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw new WrappedException(e);
            }
        };
    }
    public static void wrap(RunnableWithException runnable) {
        try {
            runnable.run();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new WrappedException(e);
        }
    }
    public static Throwable unWrap(Throwable e) {
        if (e instanceof WrappedException)
            return e.getCause();
        else return e;
    }
}
