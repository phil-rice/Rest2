package one.xingyi.core.client;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.utils.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;


public interface ISimpleList<T> extends Iterable<T> {
    int size();
    T get(int n);
    ISimpleList<T> withItem(int n, T t);
    @SafeVarargs static <T> ISimpleList<T> create(T... ts) {return new SimpleList<>(Arrays.asList(ts));}
    static <T> ISimpleList<T> fromList(List<T>ts) {return new SimpleList<>(ts);}
    default List<T> toList() {List<T> result = new ArrayList<>(); for (T t : this) result.add(t); return result;}
}

@EqualsAndHashCode
@ToString
class SimpleList<T> implements ISimpleList<T> {
    final List<T> list;
    public SimpleList(List<T> list) {
        this.list = list;
    }
    @Override public int size() {
        return list.size();
    }
    @Override public T get(int n) {
        return list.get(n);
    }
    @Override public ISimpleList<T> withItem(int n, T t) {
        ArrayList<T> result = new ArrayList<>(list);
        list.set(n, t);
        return new SimpleList<T>(result);
    }
    @Override public Iterator<T> iterator() {
        return list.iterator();
    }
}

@RequiredArgsConstructor
class MirroredSimpleList<T> implements ISimpleList<T> {
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
        return WrappedException.wrapCallable(() -> setFn.apply(n, t));
    }

    @Override public Iterator<T> iterator() {
        return new Iterator<T>() {
            int count = 0;
            @Override public boolean hasNext() { return count < size(); }
            @Override public T next() { return get(count++); }
        };
    }
}
