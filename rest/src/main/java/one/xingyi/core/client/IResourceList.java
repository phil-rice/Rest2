package one.xingyi.core.client;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import one.xingyi.core.optics.Getter;
import one.xingyi.core.optics.Lens;
import one.xingyi.core.optics.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


public interface IResourceList<T> extends ISimpleList<T> {
    IResourceList<T> withItem(int n, T t);
    IResourceList<T> append(T t);
    @SafeVarargs static <T> IResourceList<T> create(T... ts) {return new ResourceList<>(Arrays.asList(ts));}
    static <T> IResourceList<T> fromList(List<T> ts) {return new ResourceList<>(ts);}
    default List<T> toList() {List<T> result = new ArrayList<>(); for (T t : this) result.add(t); return result;}
    static <J> Lens<List<J>, IResourceList<J>> listlens() { return Lens.create(IResourceList::fromList, (ignore, list) -> list.toList()); }

}

@EqualsAndHashCode
@ToString
class ResourceList<T> implements IResourceList<T> {
    final List<T> list;
    public ResourceList(List<T> list) {
        this.list = list;
    }
    @Override public int size() {
        return list.size();
    }
    @Override public T get(int n) {
        return list.get(n);
    }
    @Override public IResourceList<T> withItem(int n, T t) {
        ArrayList<T> result = new ArrayList<>(list);
        list.set(n, t);
        return new ResourceList<T>(result);
    }
    @Override public IResourceList<T> append(T t) {
        ArrayList<T> result = new ArrayList<>(list);
        result.add(t);
        return new ResourceList<>(result);
    }
    @Override public Iterator<T> iterator() {
        return list.iterator();
    }
}

