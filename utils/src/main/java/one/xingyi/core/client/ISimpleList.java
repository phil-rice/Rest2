package one.xingyi.core.client;
import lombok.RequiredArgsConstructor;
import one.xingyi.core.utils.*;

import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.function.BiFunction;
import java.util.function.Supplier;


public interface ISimpleList<T> extends Iterable<T> {
    int size();
    T get(int n);
    ISimpleList<T> withItem(int n, T t);
}

@RequiredArgsConstructor
class SimpleList<T> implements ISimpleList<T> {
    final Object mirror;
    final Callable<Integer> sizeFn;
    final FunctionWithException<Integer, T> getFn;
    final BiFunctionWithException<Integer, T, ISimpleList<T>> setFn;
    @Override public int size() {
        return WrappedException.wrapCallable(sizeFn);
    }
    @Override public T get(int n) {
        return WrappedException.wrapFnWithE(getFn).apply(n);
    }
    @Override public ISimpleList<T> withItem(int n, T t) {
        return WrappedException.wrapCallable(() -> setFn.apply( n, t));
    }
    @Override public Iterator<T> iterator() {
        return new Iterator<T>() {
            int count = 0;
            @Override public boolean hasNext() { return count < size(); }
            @Override public T next() { return get(count++); }
        };
    }
}
