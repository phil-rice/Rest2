package one.xingyi.reference;
import one.xingyi.core.marshelling.JsonObject;
import one.xingyi.core.marshelling.JsonWriter;

import static junit.framework.TestCase.assertEquals;
public class JsonForJsonObjectWriterTests extends JsonWriterTests<JsonObject> {


    @Override protected JsonWriter<JsonObject> jsonTC() {
        return JsonWriter.cheapJson;
    }
}
