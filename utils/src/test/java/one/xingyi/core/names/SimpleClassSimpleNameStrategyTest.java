package one.xingyi.core.names;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
public class SimpleClassSimpleNameStrategyTest {
    IClassNameStrategy namesStrategy = IClassNameStrategy.simple;

    @Test public void testToRootWhenNameIs_ISomeEntity() {
        assertEquals("SomeEntity", namesStrategy.toRoot("Entity", "ISomeEntityDefn", "").result().get());
        assertEquals("Overide", namesStrategy.toRoot("Entity", "ISomeEntity", "IOveride").result().get());
    }

    @Test public void testToRootWhenNameIsNotIEndingInDefn() {
        assertEquals(List.of("<Thing> [Simple] Should start with an I", "<Thing> [Simple] Should end with Defn"), namesStrategy.toRoot("<Thing>", "Simple", "").fails());
        assertEquals(List.of("<Thing> [Isimple] Should end with Defn"), namesStrategy.toRoot("<Thing>", "Isimple", "").fails());
        assertEquals(List.of("<Thing> [SimpleDefn] Should start with an I"), namesStrategy.toRoot("<Thing>", "SimpleDefn", "").fails());
        assertEquals(List.of("<Thing> annotation [Override] in [SimpleDefn] doesn't start with an I"), namesStrategy.toRoot("<Thing>", "SimpleDefn", "Override").fails());

        assertEquals("Overide", namesStrategy.toRoot("<Thing>", "simple", "IOveride").result().get());
        assertEquals("Overide", namesStrategy.toRoot("<Thing>", "Isimple", "IOveride").result().get());
        assertEquals("Overide", namesStrategy.toRoot("<Thing>", "simpleDefn", "IOveride").result().get());
    }

    public void testToMethods() {
        //these are checked in ServerNamesTests
    }
}