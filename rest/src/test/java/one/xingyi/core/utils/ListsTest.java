package one.xingyi.core.utils;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
public class ListsTest {
    @Test
    public void testUnique() {
        assertEquals(Arrays.asList("1", "2", "3", "4"), Lists.unique(Arrays.asList("1", "2", "3", "4", "1", "2", "3", "4")));
    }
}
