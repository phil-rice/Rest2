package one.xingyi.test;
import one.xingyi.core.marshelling.ContextForJson;
import one.xingyi.core.marshelling.JsonWriter;
import one.xingyi.core.utils.Strings;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
public abstract class JsonWriterTests<J> implements IReferenceFixture3 {

    abstract protected JsonWriter<J> jsonWriter();
    String jsonString = Strings.changeQuotes("{'name':'someName','age':23,'address':{'line1':'someLine1','line2':'someLine2','postcode':'somePostcode'},'telephone':{'number':'someNumber'}}");
    @Test
    public void testToJson() {
        assertEquals(jsonString,                person.toJsonString(jsonWriter(), ContextForJson.nullContext));
    }

}
