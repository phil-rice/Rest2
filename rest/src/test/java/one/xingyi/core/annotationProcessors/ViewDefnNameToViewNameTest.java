package one.xingyi.core.annotationProcessors;
import org.junit.Test;

import static org.junit.Assert.*;

public class ViewDefnNameToViewNameTest  implements ViewDefnNameToViewNamesFixture{

    @Test public void testCanGetFromNameWithTypeToInterface() {
        assertTrue(viewToViewNames.isDefinedAt("one.view.IOneViewDefn"));
        assertEquals(one, viewToViewNames.apply("one.view.IOneViewDefn"));

    }
    //this is needed because during the lifecycle of annotations there is a good chance that the package name won't be known and will be blank
    @Test public void testCanGetFromNameWithoutTypeToInterface() {
        assertTrue(viewToViewNames.isDefinedAt("IOneViewDefn"));
        assertEquals(one, viewToViewNames.apply("IOneViewDefn"));

    }
    @Test public void testHasErrorIfNotKnown() {
        assertFalse(viewToViewNames.isDefinedAt("notin"));
        try {
            viewToViewNames.apply("notin");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Cannot have argument notin\n" +
                    "Legal values are:\n" +
                    "IOneViewDefn\n" +
                    "IPersonViewDefn\n" +
                    "ITwoViewDefn", e.getMessage());
        }

    }

}