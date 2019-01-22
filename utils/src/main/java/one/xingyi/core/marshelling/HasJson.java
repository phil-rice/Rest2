package one.xingyi.core.marshelling;
public interface HasJson<Context> {
    <J> J toJson(JsonWriter<J> jsonTc, Context context);
    default <J> String toJsonString(JsonWriter<J> jsonTc, Context context) { return jsonTc.fromJ(toJson(jsonTc, context)); }

}
