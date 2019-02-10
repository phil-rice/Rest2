package json;
import one.xingyi.core.marshelling.JsonParser;
import one.xingyi.core.marshelling.JsonParserAndWriter;
import one.xingyi.core.marshelling.JsonWriter;
import one.xingyi.core.optics.LenStoreWithJsonTest;
import one.xingyi.json.Json;
public class LenStoreWithJson extends LenStoreWithJsonTest<Object> {
    protected JsonParserAndWriter<Object> parser() { return new Json(); }
}


