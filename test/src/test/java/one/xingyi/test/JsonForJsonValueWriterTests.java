package one.xingyi.test;
import one.xingyi.core.marshelling.JsonValue;
import one.xingyi.core.marshelling.JsonWriter;

import static junit.framework.TestCase.assertEquals;
public class JsonForJsonValueWriterTests extends JsonWriterTests<JsonValue> {


    @Override protected JsonWriter<JsonValue> jsonWriter() {
        return JsonWriter.cheapJson;
    }
}
