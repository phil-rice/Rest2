package one.xingyi.core.marshelling;
public class JsonObject implements JsonValue {
    final String string;
    public JsonObject(String string) {
        this.string = string;
    }
    @Override public String toString() {
        return string;
    }
    @Override public String asString() { return string; }
}
