package one.xingyi.test;
import one.xingyi.core.client.IResourceList;
import one.xingyi.core.client.ISimpleList;
import one.xingyi.core.marshelling.ContextForJson;
import one.xingyi.core.marshelling.JsonParserAndWriter;
import one.xingyi.core.optics.Lens;
import one.xingyi.reference3.person.server.companion.PersonCompanion;
import one.xingyi.rest2test.client.viewcompanion.PrimitiveViewCompanion;
import one.xingyi.rest2test.server.companion.PrimitivesCompanion;
import one.xingyi.rest2test.server.domain.Primitives;
import org.junit.Test;

import java.util.List;
import java.util.function.Supplier;

import static junit.framework.TestCase.assertEquals;
public abstract class AbstractJsonParserWriterTests<J> extends AbstractJsonWriterTests<J> {
    abstract protected JsonParserAndWriter<J> parserWriter();

    @Test public void testCanGetDataFromJson() {
        J json = parserWriter().parse(jsonString);
        assertEquals("someName", parserWriter().asString(parserWriter().child(json, "name")));
        assertEquals(23, parserWriter().asInt(parserWriter().child(json, "age")));
    }

    @Test public void testCanLoadAPersonFromJson() {
        J json = parserWriter().parse(jsonString);
        assertEquals(person, PersonCompanion.companion.fromJson(parserWriter(), json));
    }
    <T> void check(Supplier<Lens<J, T>> supplier, T expected, T newValue) {
        J json = parserWriter().parse(jsonString);
        Lens<J, T> lens = supplier.get();
        assertEquals(expected, lens.get(json));
        J newJ = lens.set(json, newValue);
        assertEquals(expected, lens.get(json));
        assertEquals(newValue, lens.get(newJ));
    }

    @Test public void testStringLens() {
        check(() -> parserWriter().lensToString("name"), "someName", "newName");
    }

    @Test public void testIntegerLens() {
        check(() -> parserWriter().lensToInteger("age"), 23, 999);
    }

    @Test public void testChildLens() {
        check(() -> parserWriter().lensToChild("telephone").andThen(parserWriter().lensToString("number")), "someNumber", "newNumber");
    }

    @Test public void testList() {
        Lens<J, String> line1 = parserWriter().lensToString("line1");
        J json = parserWriter().parse(jsonString);
        Lens<J, IResourceList<J>> lens = parserWriter().lensToResourceList("addresses");
        IResourceList<J> addresses = lens.get(json);
        J firstChildJson = addresses.get(0);
        J secondChildJson = addresses.get(1);
        assertEquals("someLine1a", line1.get(firstChildJson));
        assertEquals("someLine1b", line1.get(secondChildJson));
    }

    @Test public void testReadingLists() {
        J json = parserWriter().parse(jsonStringWithLists);
        assertEquals(ISimpleList.fromList(List.of(1, 2, 3)), parserWriter().<Integer>asSimpleIntegerList(json, "listOfInts"));
        assertEquals(ISimpleList.fromList(List.of("one", "two", "three")), parserWriter().<Integer>asSimpleStringList(json, "listOfStrings"));
        assertEquals(ISimpleList.fromList(List.of(1.0, 2.0, 3.0)), parserWriter().<Integer>asSimpleDoubleList(json, "listOfDoubles"));
        assertEquals(ISimpleList.fromList(List.of(false, true, false)), parserWriter().<Integer>asSimpleBooleanList(json, "listOfBooleans"));
    }

    @Test public void testWritingLists() {
        Primitives primitives = new Primitives("name", 1, 2, false, true, 1.0, 2.2,
                ISimpleList.fromList(List.of("one", "two", "three")),
                ISimpleList.fromList(List.of(1, 2, 3)),
                ISimpleList.fromList(List.of(1.0, 2.1, 3.3)),
                ISimpleList.fromList(List.of(false, true, false)));
        String json = jsonWriter().fromJ(primitives.toJson(jsonWriter(), ContextForJson.nullContext));
        System.out.println(json);
        Primitives roundTrip = PrimitivesCompanion.companion.fromJson(parserWriter(), parserWriter().parse(json));
        assertEquals(primitives, roundTrip);
    }
}
