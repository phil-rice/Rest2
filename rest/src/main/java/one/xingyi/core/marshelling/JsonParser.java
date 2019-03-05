package one.xingyi.core.marshelling;

import one.xingyi.core.client.IResourceList;
import one.xingyi.core.client.ISimpleList;

import java.util.List;
public interface JsonParser<J> {
    static <J> JsonParser<J> nullParser() {return new NullParser<>();}

    String fromJ(J j);
    J parse(String jsonString);
    String asString(J j);
    default String asString(J j, String childName) {return asString(child(j, childName));}
    int asInt(J j);
    default int asInt(J j, String childName) {return asInt(child(j, childName));}
    boolean asBoolean(J j);
    default Double asDouble(J j, String childName) {return asDouble(child(j, childName));}
    Double asDouble(J j);
    default boolean asBoolean(J j, String childName) {return asBoolean(child(j, childName));}
    J child(J j, String name);
    List<J> asList(J j);
    default List<J> asList(J j, String name) {return asList(child(j, name));}
    IResourceList<J> asResourceList(J j);
    <T> ISimpleList<T> asSimpleList(J j);
    default <T>ISimpleList<T> asSimpleList(J j, String name) {return asSimpleList(child(j, name));}

    default IResourceList<J> asResourceList(J j, String name) {return asResourceList(child(j, name));}
}
class NullParser<J> implements JsonParser<J> {
    @Override public String fromJ(J j) { throw new RuntimeException("The Null Parser cannot be used");}
    @Override public J parse(String jsonString) { throw new RuntimeException("The Null Parser cannot be used to parse\n" + jsonString);}
    @Override public String asString(J j) { throw new RuntimeException("The Null Parser cannot be used");}
    @Override public int asInt(J j) { throw new RuntimeException("The Null Parser cannot be used");}
    @Override public boolean asBoolean(J j) {throw new RuntimeException("The Null Parser cannot be used");}
    @Override public Double asDouble(J j) { throw new RuntimeException("The Null Parser cannot be used");}
    @Override public J child(J j, String name) { throw new RuntimeException("The Null Parser cannot be used");}
    @Override public List<J> asList(J j) { throw new RuntimeException("The Null Parser cannot be used"); }
    @Override public IResourceList<J> asResourceList(J j) { throw new RuntimeException("The Null Parser cannot be used"); }
    @Override public <T> ISimpleList<T> asSimpleList(J j) { throw new RuntimeException("The Null Parser cannot be used"); }
}
