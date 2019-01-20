package one.xingyi.core.marshelling;
import one.xingyi.core.utils.Strings;
class CheapJson implements JsonTC<JsonObject> {
    @Override public JsonObject makeObject(Object... namesAndValues) {
        StringBuilder builder = new StringBuilder("{");
        for (int i = 0; i < namesAndValues.length; i += 2) {
            if (i != 0) builder.append(",");
            builder.append(Strings.quote(escape((String) namesAndValues[i])));
            builder.append(":");
            Object value = namesAndValues[i + 1];
            String valueString = value instanceof JsonObject ? ((JsonObject) value).string : Strings.quote(escape((String) value));
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

