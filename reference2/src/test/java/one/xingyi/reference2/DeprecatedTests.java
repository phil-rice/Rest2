package one.xingyi.reference2;
import one.xingyi.reference2.person.server.companion.PersonCompanion;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
public class DeprecatedTests {

    @Test
    public void testJavascriptForLine1() {
        assertFalse(PersonCompanion.companion.javascript, PersonCompanion.companion.javascript().contains("return lens('line1')"));
        assertTrue(PersonCompanion.companion.javascript, PersonCompanion.companion.javascript().contains(" return compose(lens_person_address_address(), lens('line1'));"));
    }
}
