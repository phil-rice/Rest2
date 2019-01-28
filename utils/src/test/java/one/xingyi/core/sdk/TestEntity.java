package one.xingyi.core.sdk;
import one.xingyi.core.marshelling.ContextForJson;
import one.xingyi.core.marshelling.HasJsonWithLinks;
import one.xingyi.core.marshelling.JsonWriter;

import java.util.function.Function;
public class TestEntity implements IXingYiEntity, HasJsonWithLinks<ContextForJson, TestEntity> {

    @Override public <J> J toJson(JsonWriter<J> jsonTc, ContextForJson contextForJson) {
        return jsonTc.makeObject("test", "json");
    }
    @Override public <J> J toJsonWithLinks(JsonWriter<J> jsonWriter, ContextForJson context, Function<TestEntity, String> stateFn) {
        return jsonWriter.makeObject("some", "json");
    }
}
