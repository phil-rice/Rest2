package one.xingyi.core.optics;
import one.xingyi.core.client.IResourceList;
import one.xingyi.core.marshelling.JsonParserAndWriter;
import one.xingyi.core.optics.lensLanguage.LensDefnStore;
import one.xingyi.core.optics.lensLanguage.LensStore;
import one.xingyi.core.optics.lensLanguage.LensStoreParser;
import one.xingyi.core.utils.Strings;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
abstract public class LenStoreWithJsonTest<J> {

    abstract protected JsonParserAndWriter<J> parser();

    @Test public void testCanAccessStringLens() {
        String json = Strings.changeQuotes("{'name':'phil','addresses':[{'line1':'someLine1'}]}");
        J j = parser().parse(json);
        LensDefnStore lensDefnStore = LensStoreParser.simple().apply("person_name=name/string");
        LensStore<J> store = lensDefnStore.makeStore(parser());
        assertEquals("phil", store.stringLens("person_name").get(j));
    }
    @Test public void testCanAccessChildLensThenString() {
        String json = Strings.changeQuotes("{'name':'phil','age':23,'addresses':[{'line1':'someLine1'}]}");
        J j = parser().parse(json);
        LensDefnStore lensDefnStore = LensStoreParser.simple().apply("person_age=age/int");
        LensStore<J> store = lensDefnStore.makeStore(parser());
        assertEquals(23, store.integerLens("person_age").get(j).intValue());
    }
    @Test public void testCanAccessChildThenString() {
        String json = Strings.changeQuotes("{'name':'phil','age':23,'address':{'line1':'someLine1'}}");
        J j = parser().parse(json);
        LensDefnStore lensDefnStore = LensStoreParser.simple().apply("person_line1=address/address,line1/string");
        LensStore<J> store = lensDefnStore.makeStore(parser());
        assertEquals("someLine1", store.integerLens("person_line1").get(j));
    }
    @Test public void testCanAccessListThenString() {
        String json = Strings.changeQuotes("{'name':'phil','age':23,'addresses':[{'line1':'someLine1a'},{'line1':'someLine1b'}]}");
        J j = parser().parse(json);
        LensDefnStore lensDefnStore = LensStoreParser.simple().apply("person_address=addresses/*address\naddress_line1=line1/string");
        LensStore<J> store = lensDefnStore.makeStore(parser());
        Lens<J, IResourceList<J>> listLens = store.listLens("person_address");
        IResourceList<J> addresses = listLens.get(j);
        assertEquals("someLine1a", store.stringLens("address_line1").get(addresses.get(0)));
        assertEquals("someLine1b", store.stringLens("address_line1").get(addresses.get(1)));
    }

    @Test public void testCanAccessFirstItemInListThenString() {
        String json = Strings.changeQuotes("{'name':'phil','age':23,'addresses':[{'line1':'someLine1a'},{'line1':'someLine1b'}]}");
        J j = parser().parse(json);
        LensDefnStore lensDefnStore = LensStoreParser.simple().apply("person_address=addresses/*address,<firstItem>,line1/string\naddress_line1=line1/string");
        LensStore<J> store = lensDefnStore.makeStore(parser());
        Lens<J, String> stringLens = store.stringLens("person_address");
        assertEquals("someLine1a", stringLens.get(j));

        J newJ = stringLens.set(j, "newLine1");
        assertEquals("someLine1a", stringLens.get(j));
        assertEquals("newLine1", stringLens.get(newJ));
    }


}
