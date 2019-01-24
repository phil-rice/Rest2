package one.xingyi.core.names;
import one.xingyi.core.validation.Result;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
public class SimpleClassSimpleNameStrategyTest {
    IClassNameStrategy namesStrategy = IClassNameStrategy.simple;

    @Test public void testToRootWhenNameIs_ISomeEntity() {
        assertEquals(Result.succeed("SomeEntity"), namesStrategy.toRoot("Entity", "ISomeEntityDefn"));
        }

    @Test public void testToRootWhenNameIsNotIEndingInDefn() {
        assertEquals(List.of("<Thing> [Simple] Should start with an I", "<Thing> [Simple] Should end with Defn"), namesStrategy.toRoot("<Thing>", "Simple").fails());
        assertEquals(List.of("<Thing> [Isimple] Should end with Defn"), namesStrategy.toRoot("<Thing>", "Isimple").fails());
        assertEquals(List.of("<Thing> [SimpleDefn] Should start with an I"), namesStrategy.toRoot("<Thing>", "SimpleDefn").fails());
    }

    public void testToMethods() {
        //these are checked in ServerNamesTests
    }
}