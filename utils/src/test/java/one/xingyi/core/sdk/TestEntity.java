package one.xingyi.core.sdk;
import one.xingyi.core.marshelling.ContextForJson;
import one.xingyi.core.marshelling.JsonWriter;
public class TestEntity implements IXingYiEntity {

    @Override public <J> J toJson(JsonWriter<J> jsonTc, ContextForJson contextForJson) {
        return jsonTc.makeObject("test", "json");
    }
}
