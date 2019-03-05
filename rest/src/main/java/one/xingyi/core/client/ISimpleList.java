package one.xingyi.core.client;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
public interface ISimpleList<T> extends Iterable<T> {
    ISimpleList<Object> empty = ISimpleList.fromList(List.of());
    static <T>ISimpleList<T> empty() { return (ISimpleList) empty;}
    int size();
    T get(int n);
    ISimpleList<T> withItem(int n, T t);
    ISimpleList<T> append(T t);
    default List<T> toList() {List<T> result = new ArrayList<>(); for (T t : this) result.add(t); return result;}

    @SafeVarargs static <T> ISimpleList<T> create(T... ts) {return new SimpleList<>(Arrays.asList(ts));}
    static <T> ISimpleList<T> fromList(List<T> ts) {return new SimpleList<>(ts);}

}
@RequiredArgsConstructor @ToString @EqualsAndHashCode
class SimpleList<T> implements ISimpleList<T> {
    final List<T> list;
    @Override public int size() { return list.size(); }
    @Override public T get(int n) { return list.get(n); }
    @Override public ISimpleList<T> withItem(int n, T t) {
        List<T> newList = new ArrayList<>(list);
        newList.set(n, t);
        return new SimpleList<>(newList);
    }
    @Override public ISimpleList<T> append(T t) {
        List<T> newList = new ArrayList<>(list);
        newList.add(t);
        return new SimpleList<>(newList);
    }
    @Override public Iterator<T> iterator() { return list.iterator(); }
}
