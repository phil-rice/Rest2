package one.xingyi.core.utils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.marshelling.ContextForJson;
import one.xingyi.core.marshelling.HasJson;
import one.xingyi.core.marshelling.HasJsonWithLinks;
import one.xingyi.core.marshelling.JsonWriter;

import java.util.function.Function;
@Getter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class IdAndValue<T> {
    public final String id;
    public final T t;
    static public <J, T extends HasJsonWithLinks<ContextForJson,T>> J toJson(IdAndValue<T> t, JsonWriter<J> jsonWriter, ContextForJson contextForJson, Function<T, String> stateFn) {
        return jsonWriter.makeObject("id", t.id, "value", t.t.toJsonWithLinks(jsonWriter, contextForJson,stateFn));
    }

}
