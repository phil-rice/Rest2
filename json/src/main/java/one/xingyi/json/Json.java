package one.xingyi.json;
import lombok.val;
import one.xingyi.core.client.ISimpleList;
import one.xingyi.core.marshelling.JsonValue;
import one.xingyi.core.marshelling.JsonParser;
import one.xingyi.core.marshelling.JsonWriter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;
public class Json implements JsonWriter<Object>, JsonParser<Object> {
    public static Json simple = new Json();
    @Override public Object makeObject(Object... namesAndValues) {
        val result = new JSONObject();
        for (int i = 0; i < namesAndValues.length; i += 2)
            result.put((String) namesAndValues[i + 0], namesAndValues[i + 1]);
        return result;
    }
    @Override public <T> Object makeList(ISimpleList<T> items) { return items.toList(); }
    @Override public Object liftString(String string) {
        return new JSONObject(string);
    }
    @Override public String fromJ(Object o) {
        return o.toString();
    }
    @Override public Object parse(String jsonString) {
        return new JSONObject(jsonString);
    }
    @Override public String asString(Object o) {
        return o.toString();
    }
    @Override public int asInt(Object o) {
        return (Integer) o;
    }
    @Override public Object child(Object o, String name) {
        if (o instanceof Map) return (((Map) o).get(name));
        else return ((JSONObject) o).get(name);
    }
    @Override public List<Object> asList(Object o) {
        return ((JSONArray) o).toList();
    }
}
