package one.xingyi.json;
import lombok.val;
import one.xingyi.core.client.IResourceList;
import one.xingyi.core.marshelling.JsonObject;
import one.xingyi.core.marshelling.JsonParser;
import one.xingyi.core.marshelling.JsonParserAndWriter;
import one.xingyi.core.marshelling.JsonWriter;
import one.xingyi.core.optics.Getter;
import one.xingyi.core.optics.Lens;
import one.xingyi.core.optics.Setter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;
public class Json implements JsonParserAndWriter<Object> {
    public static Json simple = new Json();
    @Override public Object makeObject(Object... namesAndValues) {
        val result = new JSONObject();
        for (int i = 0; i < namesAndValues.length; i += 2)
            result.put((String) namesAndValues[i + 0], namesAndValues[i + 1]);
        return result;
    }
    @Override public <T> Object makeList(IResourceList<T> items) { return items.toList(); }
    @Override public Object liftString(String string) {
        return new JSONObject(string);
    }
    @Override public String fromJ(Object o) { return o.toString(); }
    @Override public Object parse(String jsonString) {
        return new JSONObject(jsonString);
    }
    @Override public String asString(Object o) {
        return o.toString();
    }
    @Override public int asInt(Object o) { return (Integer) o; }
    @Override public Object child(Object o, String name) {
        if (o instanceof Map) return (((Map) o).get(name));
        else return ((JSONObject) o).get(name);
    }
    @Override public List<Object> asList(Object o) { return ((JSONArray) o).toList(); }
    @Override public IResourceList<Object> asResourceList(Object o) {
        return new JsonResourceList1((JSONArray) o);
    }

    JSONObject copyOf(Object j) { return new JSONObject(new HashMap<>(((JSONObject) j).map)); }

    @Override public Lens<Object, Object> lensToChild(String childname) {
        Getter<Object, Object> getter = j -> ((JSONObject) j).get(childname);
        Setter<Object, Object> setter = (j, t) -> copyOf(j).put(childname, t);
        return Lens.create(getter, setter);
    }
    @Override public Lens<Object, String> lensToString(String name) {
        Getter<Object, String> getter = j -> ((JSONObject) j).getString(name);
        Setter<Object, String> setter = (j, t) -> copyOf(j).put(name, t);
        return Lens.create(getter, setter);
    }
    @Override public Lens<Object, Double> lensToDouble(String name) {
        Getter<Object, Double> getter = j -> ((JSONObject) j).getDouble(name);
        Setter<Object, Double> setter = (j, t) -> copyOf(j).put(name, t);
        return Lens.create(getter, setter);
    }
    @Override public Lens<Object, Integer> lensToInteger(String name) {
        Getter<Object, Integer> getter = j -> ((JSONObject) j).getInt(name);
        Setter<Object, Integer> setter = (j, t) -> copyOf(j).put(name, t);
        return Lens.create(getter, setter);
    }
    @Override public Lens<Object, IResourceList<Object>> lensToResourceList(String name) {
        Getter<Object, IResourceList<Object>> getter = j -> asResourceList(j, name);
        Setter<Object, IResourceList<Object>> setter = (j, rl) -> copyOf(j).put(name, rl.toList());
        return Lens.create(getter, setter);
    }
}
