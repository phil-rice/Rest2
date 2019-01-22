package one.xingyi.core.marshelling;
public interface JsonParser<J> {
    String fromJ(J j);
    J parse(String jsonString);
    String asString(J j);
    int asInt(J j);
    J child(J j,String name);
}
