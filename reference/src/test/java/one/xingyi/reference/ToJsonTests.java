package one.xingyi.reference;
import one.xingyi.core.marshelling.ContextForJson;
import one.xingyi.core.marshelling.JsonTC;
import one.xingyi.core.utils.Strings;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
public class ToJsonTests implements IReferenceFixture {

    JsonTC jsonTC = JsonTC.cheapJson;
    @Test
    public void testToJson() {
        assertEquals(Strings.changeQuotes(
                "{'name':'someName','age':23,'address':{'line1':'someLine1','line2':'someLine2','postcode':'somePostcode'},'telephone':{'number':'someNumber'}}"),
                person.toJsonString(jsonTC, ContextForJson.nullContext));
    }
}
