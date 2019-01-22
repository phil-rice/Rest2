package one.xingyi.test;
import one.xingyi.core.marshelling.JsonWriter;
import one.xingyi.json.Json;
import one.xingyi.reference.JsonWriterTests;
public class JsonWriterTest extends JsonWriterTests<Object> {
    @Override protected JsonWriter<Object> jsonTC() {
        return new Json();
    }
}
