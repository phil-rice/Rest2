package one.xingyi.core.utils;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
public class StringsTest {
    @Test public void testStringsFrom() {
        assertEquals(Optional.empty(), Strings.from(null));
        assertEquals(Optional.empty(), Strings.from(""));
        assertEquals(Optional.of("a"), Strings.from("a"));
    }

    @Test
    public void testExtractWhenPresent() {
        String actual = Strings.extractFromOptionalEnvelope(List.class.getName() + "<", ">", List.class.getName() + "<one.xingyi.restExample.ITelephoneNumber>");
        assertEquals("one.xingyi.restExample.ITelephoneNumber", actual);
    }
    @Test
    public void testExtractWhenNotPresentAtEnd() {
        String actual = Strings.extractFromOptionalEnvelope(List.class.getName() + "<", ">", List.class.getName() + "<one.xingyi.restExample.ITelephoneNumber");
        assertEquals("java.util.List<one.xingyi.restExample.ITelephoneNumber", actual);
    }
    @Test
    public void testExtractWhenNotPresentAtStart() {
        String actual = Strings.extractFromOptionalEnvelope(List.class.getName() + "<", ">", "something.List<one.xingyi.restExample.ITelephoneNumber>");
        assertEquals("something.List<one.xingyi.restExample.ITelephoneNumber>", actual);
    }

}