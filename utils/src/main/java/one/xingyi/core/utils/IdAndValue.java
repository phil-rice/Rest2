package one.xingyi.core.utils;
import lombok.RequiredArgsConstructor;
import one.xingyi.core.marshelling.ContextForJson;
import one.xingyi.core.marshelling.HasJson;
import one.xingyi.core.marshelling.JsonWriter;
@RequiredArgsConstructor
public class IdAndValue<T> {
  public  final String id;
    public   final T t;
    static public <J, T extends HasJson<ContextForJson>> J toJson(IdAndValue<T> t, String tName, JsonWriter<J> jsonTc, ContextForJson contextForJson) {
        return jsonTc.makeObject("id", t.id, tName, t.t.toJson(jsonTc, contextForJson));
    }
}
