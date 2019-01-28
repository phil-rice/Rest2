package one.xingyi.core.marshelling;
public interface JsonParser<J> {
    static <J> JsonParser<J> nullParser() {return new NullParser<>();}

    String fromJ(J j);
    J parse(String jsonString);
    String asString(J j);
    default String asString(J j, String childName) {return asString(child(j, childName));}
    int asInt(J j);
    default int asInt(J j, String childName) {return asInt(child(j, childName));}
    J child(J j, String name);
}
class NullParser<J> implements JsonParser<J> {
    @Override public String fromJ(J j) { throw new RuntimeException("The Null Parser cannot be used");}
    @Override public J parse(String jsonString) { throw new RuntimeException("The Null Parser cannot be used");}
    @Override public String asString(J j) { throw new RuntimeException("The Null Parser cannot be used");}
    @Override public int asInt(J j) { throw new RuntimeException("The Null Parser cannot be used");}
    @Override public J child(J j, String name) { throw new RuntimeException("The Null Parser cannot be used");}
}
