package one.xingyi.core.marshelling;
import one.xingyi.core.utils.Strings;
class CheapJson implements JsonWriter<JsonObject> {
    public String jsonValueFor(Object obj){
        if (obj instanceof JsonObject) return ((JsonObject) obj).string;
        if (obj instanceof Integer) return obj.toString();
        if (obj instanceof Double) return obj.toString();
        if (obj instanceof Boolean) return obj.toString();
        return Strings.quote(obj.toString());
    }

    @Override public JsonObject makeObject(Object... namesAndValues) {
        StringBuilder builder = new StringBuilder("{");
        for (int i = 0; i < namesAndValues.length; i += 2) {
            if (i != 0) builder.append(",");
            builder.append(Strings.quote(escape((String) namesAndValues[i])));
            builder.append(":");
            Object value = namesAndValues[i + 1];
            String valueString = jsonValueFor(value);
            builder.append(valueString);
        }
        return new JsonObject(builder + "}");
    }
    @Override public JsonObject liftString(String string) {
        return new JsonObject(Strings.quote(escape(string)));
    }
    @Override public String fromJ(JsonObject jsonObject) {
        return jsonObject.string;
    }

    String escape(String s) {
        return s.replace("\b", "\\b").
                replace("\f", "\\f").
                replace("\n", "\\n").
                replace("\r", "\\r").
                replace("\t", "\\t").
                replace("\"", "\\\"").
                replace("\\", "\\");

    }
}

