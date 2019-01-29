package one.xingyi.trafficlights;
import one.xingyi.core.reflection.Reflection;
import one.xingyi.core.utils.WrappedException;
import one.xingyi.trafficlights.client.view.ColourView;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.fail;
public class PostAnnotationTest {

    @Test
    public void testPostAnnotationsDontCreateGetterAndSetterFields() {
        try {
            new Reflection<>(ColourView.class).method0("orange");
            fail();
        } catch (WrappedException e) {
            assertEquals(NoSuchMethodException.class, e.getCause().getClass());
        }
    }
}
