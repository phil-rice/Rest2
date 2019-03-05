package one.xingyi.core.optics;
import one.xingyi.core.client.IResourceList;
import one.xingyi.core.client.ISimpleList;
import one.xingyi.core.marshelling.JsonParserAndWriter;
import one.xingyi.core.optics.lensLanguage.LensDefnStore;
import one.xingyi.core.optics.lensLanguage.LensStore;
import one.xingyi.core.optics.lensLanguage.LensStoreParser;
import one.xingyi.core.utils.Strings;
import org.junit.Test;

import java.util.List;

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
    @Test public void testCanChangeListThenString() {
        String json = Strings.changeQuotes("{'name':'phil','age':23,'addresses':[{'line1':'someLine1a'},{'line1':'someLine1b'}]}");
        J j = parser().parse(json);
        LensDefnStore lensDefnStore = LensStoreParser.simple().apply("person_address=addresses/*address\naddress_line1=line1/string");
        LensStore<J> store = lensDefnStore.makeStore(parser());
        Lens<J, IResourceList<J>> listLens = store.listLens("person_address");
        Lens<J, String> stringLens = store.stringLens("address_line1");
        IResourceList<J> addresses = listLens.get(j);
        IResourceList<J> newAddresses = addresses.withItem(0, stringLens.set(addresses.get(0), "newLine1"));

        assertEquals("someLine1a", store.stringLens("address_line1").get(addresses.get(0)));
        assertEquals("someLine1b", store.stringLens("address_line1").get(addresses.get(1)));

        assertEquals("newLine1", store.stringLens("address_line1").get(newAddresses.get(0)));
        assertEquals("someLine1b", store.stringLens("address_line1").get(newAddresses.get(1)));

        J newJ = listLens.set(j, newAddresses);
        assertEquals("someLine1a", stringLens.get(listLens.get(j).get(0)));
        assertEquals("newLine1", stringLens.get(listLens.get(newJ).get(0)));
    }

    @Test public void testCanAccessFirstItemInListThenString() {
        String json = Strings.changeQuotes("{'name':'phil','age':23,'addresses':[{'line1':'someLine1a'},{'line1':'someLine1b'}]}");
        J j = parser().parse(json);
        LensDefnStore lensDefnStore = LensStoreParser.simple().apply("person_address=addresses/*address,{firstItem},line1/string\naddress_line1=line1/string");
        LensStore<J> store = lensDefnStore.makeStore(parser());
        Lens<J, String> stringLens = store.stringLens("person_address");
        assertEquals("someLine1a", stringLens.get(j));

        J newJ = stringLens.set(j, "newLine1");
        assertEquals("someLine1a", stringLens.get(j));
        assertEquals("newLine1", stringLens.get(newJ));
    }

    @Test public void testCanUseIdentity() {
        String json = Strings.changeQuotes("{'name':'phil','age':23, 'line1':'someLine1a', 'line2':'someLine2'}");
        J j = parser().parse(json);
        LensDefnStore lensDefnStore = LensStoreParser.simple().apply("person_line1={identity},line1/String");
        LensStore<J> store = lensDefnStore.makeStore(parser());
        Lens<J, String> stringLens = store.stringLens("person_line1");
        assertEquals("someLine1a", stringLens.get(j));

        J newJ = stringLens.set(j, "newLine1");
        assertEquals("someLine1a", stringLens.get(j));
        assertEquals("newLine1", stringLens.get(newJ));

    }

    @Test public void testCanUseItemAsList() {
        String json = Strings.changeQuotes("{'name':'phil','age':23,'addresses':{'line1':'someLine1a'}}}");
        J j = parser().parse(json);
        LensDefnStore lensDefnStore = LensStoreParser.simple().apply("person_address=addresses/address,{itemAsList}\naddress_line1=line1/string");
        LensStore<J> store = lensDefnStore.makeStore(parser());
        Lens<J, IResourceList<J>> listLens = store.listLens("person_address");
        IResourceList<J> addresses = listLens.get(j);
        assertEquals("someLine1a", store.stringLens("address_line1").get(addresses.get(0)));
    }

    @Test public void testCanUseSimpleStringLists() {
        String json = Strings.changeQuotes("{'items':['a','b']}");
        J j = parser().parse(json);
        LensDefnStore lensDefnStore = LensStoreParser.simple().apply("list_Stringlist=items/**string");
        LensStore<J> store = lensDefnStore.makeStore(parser());
        assertEquals(ISimpleList.fromList(List.of("a", "b")), store.simpleListLens("list_Stringlist").get(j));
    }

    @Test public void testCanUseSimpleIntegerLists() {
        String json = Strings.changeQuotes("{'items':[1,2]}");
        J j = parser().parse(json);
        LensDefnStore lensDefnStore = LensStoreParser.simple().apply("list_Stringlist=items/**integer");
        LensStore<J> store = lensDefnStore.makeStore(parser());
        assertEquals(ISimpleList.fromList(List.of(1, 2)), store.simpleListLens("list_Stringlist").get(j));
    }

    @Test public void testCanUseSimpleBooleanLists() {
        String json = Strings.changeQuotes("{'items':[false,true]}");
        J j = parser().parse(json);
        LensDefnStore lensDefnStore = LensStoreParser.simple().apply("list_Stringlist=items/**boolean");
        LensStore<J> store = lensDefnStore.makeStore(parser());
        assertEquals(ISimpleList.fromList(List.of(false, true)), store.simpleListLens("list_Stringlist").get(j));
    }
    @Test public void testCanUseSimpleDoubleLists() {
        String json = Strings.changeQuotes("{'items':[1,2.1]}");
        J j = parser().parse(json);
        LensDefnStore lensDefnStore = LensStoreParser.simple().apply("list_Stringlist=items/**double");
        LensStore<J> store = lensDefnStore.makeStore(parser());
        assertEquals(ISimpleList.fromList(List.of(1.0, 2.1)), store.simpleListLens("list_Stringlist").get(j));
    }


}
