package json;
import one.xingyi.core.marshelling.JsonParser;
import one.xingyi.core.marshelling.JsonWriter;
import one.xingyi.core.optics.LenStoreWithJsonTest;
import one.xingyi.json.Json;
public class LenStoreWithJson extends LenStoreWithJsonTest<Object> {
    protected JsonParser<Object> parser() { return new Json(); }
    protected JsonWriter<Object> writer() { return new Json(); }
}


