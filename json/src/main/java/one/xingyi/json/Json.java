package one.xingyi.json;
import lombok.val;
import one.xingyi.core.marshelling.JsonObject;
import one.xingyi.core.marshelling.JsonParser;
import one.xingyi.core.marshelling.JsonWriter;
import org.json.JSONObject;
import org.json.JSONString;
public class Json implements JsonWriter<Object>, JsonParser<Object> {
    public static Json simple = new Json();
    @Override public Object makeObject(Object... namesAndValues) {
        val result = new JSONObject();
        for (int i = 0; i < namesAndValues.length; i += 2)
            result.put((String) namesAndValues[i + 0], namesAndValues[i + 1]);
        return result;
    }
    @Override public Object liftString(String string) {
        return new JsonObject(string);
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
        return ((JSONObject) o).get(name);
    }
}
