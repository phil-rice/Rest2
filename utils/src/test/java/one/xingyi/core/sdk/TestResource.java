package one.xingyi.core.sdk;
import one.xingyi.core.marshelling.ContextForJson;
import one.xingyi.core.marshelling.HasJsonWithLinks;
import one.xingyi.core.marshelling.JsonWriter;

import java.util.function.Function;
public class TestResource implements IXingYiResource, HasJsonWithLinks<ContextForJson, TestResource> {

    @Override public <J> J toJson(JsonWriter<J> jsonTc, ContextForJson contextForJson) {
        return jsonTc.makeObject("test", "parserAndWriter");
    }
    @Override public <J> J toJsonWithLinks(JsonWriter<J> jsonWriter, ContextForJson context, Function<TestResource, String> stateFn) {
        return jsonWriter.makeObject("some", "parserAndWriter");
    }
}
