package one.xingyi.core.optics;
import one.xingyi.core.marshelling.JsonParser;
import one.xingyi.core.marshelling.JsonWriter;
import one.xingyi.core.optics.lensLanguage.LensStore;
import one.xingyi.core.optics.lensLanguage.LensStoreParser;
import one.xingyi.core.utils.Strings;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
abstract public class LenStoreWithJsonTest<J> {

    abstract protected JsonParser<J> parser();
    abstract protected JsonWriter<J> writer();

    @Test public void testCanAccessJsonUsingParser() {
        String json = Strings.changeQuotes("{'name':'phil','addresses':[{'line1':'someLine1'}]}");
        J j = parser().parse(json);
        LensStore lensStore = LensStoreParser.simple().apply("person_name=name/string\nperson_addresses=addresses/addresses\naddress_line1=line1/string");
        assertEquals("phil", lensStore.stringLens("person_name").get(j));

    }
}
