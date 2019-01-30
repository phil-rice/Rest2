package one.xingyi.test;
import one.xingyi.core.marshelling.JsonObject;
import one.xingyi.core.marshelling.JsonValue;
import one.xingyi.core.marshelling.JsonWriter;
import one.xingyi.json.Json;

import static one.xingyi.core.marshelling.JsonWriter.cheapJson;
public class CheapJsonWriterTest extends AbstractJsonWriterTests<JsonValue> {
    @Override protected JsonWriter<JsonValue> jsonWriter() {
        return cheapJson;
    }
}
