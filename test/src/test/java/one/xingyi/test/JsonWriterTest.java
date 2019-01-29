package one.xingyi.test;
import one.xingyi.core.marshelling.JsonWriter;
import one.xingyi.json.Json;
public class JsonWriterTest extends JsonWriterTests<Object> {
    @Override protected JsonWriter<Object> jsonWriter() {
        return new Json();
    }
}
