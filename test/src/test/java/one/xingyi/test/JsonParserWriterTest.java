package one.xingyi.test;
import one.xingyi.core.marshelling.JsonParser;
import one.xingyi.core.marshelling.JsonWriter;
import one.xingyi.json.Json;
public class JsonParserWriterTest extends AbstractJsonParserWriterTests<Object> {
    @Override protected JsonWriter<Object> jsonWriter() {
        return new Json();
    }
    @Override protected JsonParser<Object> jsonParser() {
        return new Json();
    }
}
