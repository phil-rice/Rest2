package one.xingyi.test;
import one.xingyi.core.marshelling.ContextForJson;
import one.xingyi.core.marshelling.JsonParser;
import one.xingyi.core.marshelling.JsonWriter;
import one.xingyi.core.utils.Strings;
import one.xingyi.reference3.person.server.companion.PersonCompanion;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
public abstract class AbstractJsonWriterTests<J>  implements IReferenceFixture3 {

    String jsonString = Strings.changeQuotes("{'name':'someName','age':23,'addresses':[{'line1':'someLine1a','line2':'someLine2a','postcode':'somePostcode1'},{'line1':'someLine1b','line2':'someLine2b','postcode':'somePostcode2'}],'telephone':{'number':'someNumber'}}");
    abstract protected JsonWriter<J> jsonWriter();

    @Test public void testCanMakeJson(){
        assertEquals(jsonString, jsonWriter().fromJ(person.toJson(jsonWriter(), ContextForJson.nullContext)));
    }

}
