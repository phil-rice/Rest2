package one.xingyi.core.marshelling;
public interface HasJson<Context> {
    <J> J toJson(JsonWriter<J> jsonWriter, Context context);
    default <J> String toJsonString(JsonWriter<J> jsonWriter, Context context) { return jsonWriter.fromJ(toJson(jsonWriter, context)); }

}
