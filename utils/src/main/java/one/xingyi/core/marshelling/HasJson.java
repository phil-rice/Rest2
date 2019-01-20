package one.xingyi.core.marshelling;
public interface HasJson<Context> {
    <J> J toJson(JsonTC<J> jsonTc, Context context);
    default <J> String toJsonString(JsonTC<J> jsonTc, Context context) { return jsonTc.fromJ(toJson(jsonTc, context)); }

}
