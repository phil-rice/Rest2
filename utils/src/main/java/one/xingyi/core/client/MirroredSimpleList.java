package one.xingyi.core.client;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import lombok.ToString;

import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
@ToString
public class MirroredSimpleList<T> implements ISimpleList<T> {
    public final ScriptObjectMirror mirror;
    private final List<Object> asList;
    private Function<Object, T> maker;
    private BiFunction<Integer, T, ScriptObjectMirror> setter;
    private Function<T, ScriptObjectMirror> addFn;
    public MirroredSimpleList(ScriptObjectMirror mirror, Function<Object, T> maker, BiFunction<Integer, T, ScriptObjectMirror> setter, Function<T, ScriptObjectMirror> addFn) {
        this.mirror = mirror;
        this.asList = mirror.to(List.class);
        this.maker = maker;
        this.setter = setter;
        this.addFn = addFn;
    }
    @Override public int size() {
        return asList.size();
    }
    @Override public T get(int n) { return maker.apply(asList.get(0)); }
    @Override public ISimpleList<T> withItem(int n, T t) {
        return new MirroredSimpleList<>(setter.apply(n, t), maker, setter, addFn);
    }
    @Override public ISimpleList<T> append(T t) {
        return new MirroredSimpleList<T>(addFn.apply(t), maker, setter, addFn);
    }

    @Override public Iterator<T> iterator() {
        return new Iterator<T>() {
            int count = 0;
            @Override public boolean hasNext() { return count < size(); }
            @Override public T next() { return get(count++); }
        };
    }
}
