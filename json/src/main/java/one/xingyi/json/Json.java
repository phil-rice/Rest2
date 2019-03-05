package one.xingyi.json;
import lombok.val;
import one.xingyi.core.client.IResourceList;
import one.xingyi.core.client.ISimpleList;
import one.xingyi.core.marshelling.JsonParserAndWriter;
import one.xingyi.core.optics.Getter;
import one.xingyi.core.optics.Lens;
import one.xingyi.core.optics.Setter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;
import java.util.function.BiFunction;
public class Json implements JsonParserAndWriter<Object> {
    public static Json simple = new Json();
    @Override public Object makeObject(Object... namesAndValues) {
        val result = new JSONObject();
        for (int i = 0; i < namesAndValues.length; i += 2)
            result.put((String) namesAndValues[i + 0], namesAndValues[i + 1]);
        return result;
    }
    @Override public <T> Object makeList(IResourceList<T> items) { return items.toList(); }
    @Override public <T> Object makeSimpleList(ISimpleList<T> items) { return items.toList(); }
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
    @Override public boolean asBoolean(Object o) { return (Boolean) o; }

    @Override public Double asDouble(Object o) { return (Double) o; }

    @Override public Double asDouble(Object o, String childName) {
        if (o instanceof Map) return (Double) ((Map) o).get(childName);
        else return ((JSONObject) o).getDouble(childName);
    }
    @Override public Object child(Object o, String name) {
        if (o instanceof Map) return (((Map) o).get(name));
        else return ((JSONObject) o).get(name);
    }
    @Override public List<Object> asList(Object o) { return ((JSONArray) o).toList(); }
    @Override public IResourceList<Object> asResourceList(Object o) {
        return new JsonResourceList1((JSONArray) o);
    }


    public static <T> ISimpleList<T> mapFromJsonArray(Object o, BiFunction<JSONArray, Integer, T> fn) {
        List<T> result = new ArrayList<>();
        JSONArray array = (JSONArray) o;
        for (int i = 0; i < array.length(); i++)
            result.add(fn.apply(array, i));
        return ISimpleList.fromList(result);
    }

    @Override public ISimpleList<String> asSimpleStringList(Object o) { return mapFromJsonArray(o, JSONArray::getString); }
    @Override public ISimpleList<Integer> asSimpleIntegerList(Object o) { return mapFromJsonArray(o, JSONArray::getInt); }
    @Override public ISimpleList<Double> asSimpleDoubleList(Object o) { return mapFromJsonArray(o, JSONArray::getDouble); }
    @Override public ISimpleList<Boolean> asSimpleBooleanList(Object o) { return mapFromJsonArray(o, JSONArray::getBoolean); }

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

    <T> ISimpleList<T> simpleList(Object j, String name, String primitiveClassName) {
        switch (primitiveClassName.toLowerCase()) {
            case "string":
                return (ISimpleList<T>) asSimpleStringList(j, name);
            case "integer":
                return (ISimpleList<T>) asSimpleIntegerList(j, name);
            case "double":
                return (ISimpleList<T>) asSimpleDoubleList(j, name);
            case "boolean":
                return (ISimpleList<T>) asSimpleBooleanList(j, name);
        }
        throw new IllegalArgumentException("Don't know how to make a simple list of " + primitiveClassName + " legal values are string,integer,boolean,double");
    }
    @Override public <T> Lens<Object, ISimpleList<T>> lensToSimpleList(String name, String primitiveClassName) {
        Getter<Object, ISimpleList<T>> getter = j -> simpleList(j, name, primitiveClassName);
        Setter<Object, ISimpleList<T>> setter = (j, rl) -> copyOf(j).put(name, rl.toList());
        return Lens.create(getter, setter);
    }
    @Override public Lens<Object, IResourceList<Object>> lensToSimpleList(String name) {
        Getter<Object, IResourceList<Object>> getter = j -> asResourceList(j, name);
        Setter<Object, IResourceList<Object>> setter = (j, rl) -> copyOf(j).put(name, rl.toList());
        return Lens.create(getter, setter);
    }
}
