package one.xingyi.core.marshelling;
import one.xingyi.core.client.IResourceList;

import java.util.LinkedHashMap;
import java.util.List;
public interface JsonWriter<J> {

    default <Context> String toJson(HasJson<Context> hasJson, Context context) {
        return fromJ(hasJson.toJson(this, context));
    }

    /**
     * Contract is that there are an even number and that the first is a string, the next is a J
     */
    J makeObject(Object... namesAndValues);
    <T extends Object> J makeList(IResourceList<T> items);
    default J makeList(List<Object> items) {return makeList(IResourceList.fromList(items));}
    J liftString(String string);
    String fromJ(J j);


    static JsonWriter<Object> forMaps = new JsonWriterForMaps();
    static JsonWriter<JsonValue> cheapJson = new CheapJson();
}


class JsonWriterForMaps implements JsonWriter<Object> {

    @Override public Object makeObject(Object... namesAndValues) {
        if (namesAndValues.length % 2 != 0)
            throw new RuntimeException("Cannot call this method size is: " + namesAndValues.length);
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        for (int i = 0; i < namesAndValues.length; i += 2) {
            result.put((String) namesAndValues[i], namesAndValues[i + 1]);
        }
        return result;
    }
    @Override public <T> Object makeList(IResourceList<T> items) { return items.toList(); }
    @Override public Object liftString(String string) {
        return string;
    }
    @Override public String fromJ(Object o) {
        return o.toString();
    }
}
