package one.xingyi.core.marshelling;
public interface MakesFromJson<T> {
    <J> T fromJson(JsonParser<J> jsonParser, J j);
}
