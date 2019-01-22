package one.xingyi.reference;
import one.xingyi.core.marshelling.JsonParser;
import one.xingyi.core.marshelling.JsonWriter;
import one.xingyi.core.utils.Strings;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
public abstract class JsonParserWriterTests<J> {

    String jsonString = Strings.changeQuotes("{'name':'someName','age':23,'address':{'line1':'someLine1','line2':'someLine2','postcode':'somePostcode'},'telephone':{'number':'someNumber'}}");
    abstract protected JsonWriter<J> jsonWriter();
    abstract protected JsonParser<J> jsonParser();

    @Test
    public void testCanGetDataFromJson() {
        J json = jsonParser().parse(jsonString);
        assertEquals("someName", jsonParser().asString(jsonParser().child(json, "name")));
        assertEquals(23, jsonParser().asInt(jsonParser().child(json, "age")));
        assertEquals("someLine1", jsonParser().asString(jsonParser().child(jsonParser().child(json, "address"), "line1")));
    }
}
