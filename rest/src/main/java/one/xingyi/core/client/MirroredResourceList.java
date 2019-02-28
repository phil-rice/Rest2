package one.xingyi.core.client;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import lombok.ToString;
import one.xingyi.core.utils.Function3;

import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
@ToString
public class MirroredResourceList<T> implements IResourceList<T> {
    public final ScriptObjectMirror mirror;
    private final List<Object> asList;
    private Function<Object, T> maker;
    private Function3<ScriptObjectMirror, Integer, T, ScriptObjectMirror> setter;
    private BiFunction<ScriptObjectMirror, T, ScriptObjectMirror> addFn;
    public MirroredResourceList(ScriptObjectMirror mirror, Function<Object, T> maker, Function3<ScriptObjectMirror, Integer, T, ScriptObjectMirror> setter, BiFunction<ScriptObjectMirror, T, ScriptObjectMirror> addFn) {
        this.mirror = mirror;
        this.asList = mirror.to(List.class);
        this.maker = maker;
        this.setter = setter;
        this.addFn = addFn;
    }
    @Override public int size() {
        return asList.size();
    }
    @Override public T get(int n) { return maker.apply(asList.get(n)); }
    @Override public IResourceList<T> withItem(int n, T t) {
        return new MirroredResourceList<>(setter.apply(mirror, n, t), maker, setter, addFn);
    }
    @Override public IResourceList<T> append(T t) {
        return new MirroredResourceList<T>(addFn.apply(mirror, t), maker, setter, addFn);
    }

    @Override public Iterator<T> iterator() {
        return new Iterator<T>() {
            int count = 0;
            @Override public boolean hasNext() { return count < size(); }
            @Override public T next() { return get(count++); }
        };
    }
}
