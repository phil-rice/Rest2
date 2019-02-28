package one.xingyi.core.utils;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
public class OptionalsTest {

    @Test public void testFrom() {
        assertEquals(Optional.empty(), Optionals.from(false, "anything"));
        assertEquals(Optional.of("abc"), Optionals.from(true, "abc"));
    }
    @Test public void testFromSupplier() {
        assertEquals(Optional.empty(), Optionals.from(false, () -> "anything"));
        assertEquals(Optional.of("abc"), Optionals.from(true, () -> "abc"));
    }

}