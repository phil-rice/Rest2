package one.xingyi.test;
import one.xingyi.core.marshelling.JsonParser;
import one.xingyi.core.marshelling.JsonValue;
import one.xingyi.core.marshelling.JsonWriter;

import static one.xingyi.core.marshelling.JsonWriter.cheapJson;
public class CheapJsonWriterTest extends AbstractJsonWriterTests<JsonValue> {
    @Override protected JsonWriter<JsonValue> jsonWriter() {
        return cheapJson;
    }
}
