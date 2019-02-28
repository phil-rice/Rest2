package one.xingyi.core.client;
import lombok.ToString;
import one.xingyi.core.marshelling.JsonParserAndWriter;
import one.xingyi.core.utils.Function3;

import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
@ToString
public class JsonResourceList<J,T> implements IResourceList<T> {
    private JsonParserAndWriter<J> json;
    public final J mirror;
    private final List<J> asList;
    private Function<J, T> maker;
    private Function3<J, Integer, T, J> setter;
    private BiFunction<J, T, J> addFn;
    public JsonResourceList(JsonParserAndWriter<J> json, J mirror, Function<J, T> maker, Function3<J, Integer, T, J> setter, BiFunction<J, T, J> addFn) {
        this.json = json;
        this.mirror = mirror;
        this.asList = json.asList(mirror);
        this.maker = maker;
        this.setter = setter;
        this.addFn = addFn;
    }
    @Override public int size() {
        return asList.size();
    }
    @Override public T get(int n) { return maker.apply(asList.get(n)); }
    @Override public IResourceList<T> withItem(int n, T t) {
        return new JsonResourceList<J,T>(json,setter.apply(mirror, n, t), maker, setter, addFn);
    }
    @Override public IResourceList<T> append(T t) {
        return new JsonResourceList<>(json,addFn.apply(mirror, t), maker, setter, addFn);
    }

    @Override public Iterator<T> iterator() {
        return new Iterator<T>() {
            int count = 0;
            @Override public boolean hasNext() { return count < size(); }
            @Override public T next() { return get(count++); }
        };
    }
}
