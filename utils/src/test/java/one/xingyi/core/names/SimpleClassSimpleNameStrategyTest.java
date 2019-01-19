package one.xingyi.core.names;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
public class SimpleClassSimpleNameStrategyTest {
    IClassNameStrategy namesStrategy = IClassNameStrategy.simple;

    @Test public void testToRootWhenNameIs_ISomeEntity() {
        assertEquals("SomeEntity", namesStrategy.toRoot("ISomeEntity", ""));
        assertEquals("override", namesStrategy.toRoot("ISomeEntity", "override"));
    }

    @Test public void testToRootWhenNameIs_IIomeEntity() {
        assertEquals("IsGood", namesStrategy.toRoot("IIsGood", ""));
        assertEquals("override", namesStrategy.toRoot("IIsGood", "override"));
        assertEquals("IEntity", namesStrategy.toRoot("I", ""));
        assertEquals("override", namesStrategy.toRoot("I", "override"));
    }

    @Test public void testToRootWhenNameIs_SomeEntity() {
        assertEquals("SomeEntityEntity", namesStrategy.toRoot("SomeEntity", ""));
        assertEquals("override", namesStrategy.toRoot("SomeEntity", "override"));
    }

    public void testToMethods(){
        //these are checked in ServerNamesTests
    }
}