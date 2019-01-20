package one.xingyi.core.marshelling;
import java.util.LinkedHashMap;
public interface JsonTC<J> {

    default <Context> String toJson(HasJson<Context> hasJson, Context context) {
        return fromJ(hasJson.toJson(this, context));
    }

    /**
     * Contract is that there are an even number and that the first is a string, the next is a J
     */
    J makeObject(Object... namesAndValues);
    J liftString(String string);
    String fromJ(J j);

    static JsonTC<Object> forMaps = new JsonTCForMaps();
    static JsonTC<JsonObject> cheapJson = new CheapJson();
}


class JsonTCForMaps implements JsonTC<Object> {

    @Override public Object makeObject(Object... namesAndValues) {
        if (namesAndValues.length % 2 != 0)
            throw new RuntimeException("Cannot call this method size is: " + namesAndValues.length);
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        for (int i = 0; i < namesAndValues.length; i += 2) {
            result.put((String) namesAndValues[i], namesAndValues[i + 1]);
        }
        return result;
    }
    @Override public Object liftString(String string) {
        return string;
    }
    @Override public String fromJ(Object o) {
        return o.toString();
    }
}
