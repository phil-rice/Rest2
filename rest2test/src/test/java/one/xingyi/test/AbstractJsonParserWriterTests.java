package one.xingyi.test;
import one.xingyi.core.client.IResourceList;
import one.xingyi.core.marshelling.JsonParserAndWriter;
import one.xingyi.core.optics.Lens;
import one.xingyi.reference3.person.server.companion.PersonCompanion;
import org.junit.Test;

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
}
