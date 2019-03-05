package one.xingyi.core.optics;
import one.xingyi.core.optics.lensLanguage.*;
import one.xingyi.core.utils.Lists;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;
public class LensLanguageTest {
    LensValueParser lensParser = LensValueParser.simple();
    LensLineParser lineParser = LensLineParser.simple();
    LensStoreParser storeParser = LensStoreParser.simple();

    @Test public void testValueParser() {
        assertEquals(List.of(new StringLensDefn("line1")), lensParser.apply("line1/string"));
        assertEquals(List.of(new IntegerLensDefn("line1")), lensParser.apply("line1/integer"));
        assertEquals(List.of(new DoubleLensDefn("line1")), lensParser.apply("line1/double"));
        assertEquals(List.of(new ViewLensDefn("a", "address")), lensParser.apply("a/address"));
        assertEquals(List.of(new ListLensDefn("a", "address")), lensParser.apply("a/*address"));
        assertEquals(List.of(new FirstItemInListDefn<Object>()), lensParser.apply("{firstItem}"));
        assertEquals(List.of(new IdentityDefn()), lensParser.apply("{identity}"));
        assertEquals(List.of(new ItemAsListDefn()), lensParser.apply("{itemAsList}"));
    }
    @Test public void testChildThenValueParser() {
        assertEquals(List.of(new ViewLensDefn("child", "childClass"), new StringLensDefn("line1")), lensParser.apply("child/childClass,line1/string"));
        assertEquals(List.of(new ViewLensDefn("child", "childClass"), new IntegerLensDefn("line1")), lensParser.apply("child/childClass,line1/integer"));
        assertEquals(List.of(new ViewLensDefn("child", "childClass"), new DoubleLensDefn("line1")), lensParser.apply("child/childClass,line1/double"));
        assertEquals(List.of(new ViewLensDefn("child", "childClass"), new ViewLensDefn("a", "address")), lensParser.apply("child/childClass,a/address"));
        assertEquals(List.of(new ViewLensDefn("child", "childClass"), new ListLensDefn("a", "address")), lensParser.apply("child/childClass,a/*address"));
        assertEquals(List.of(new ViewLensDefn("child", "childClass"), new SimpleListLensDefn<>("a", "string")), lensParser.apply("child/childClass,a/**string"));
        assertEquals(List.of(new ViewLensDefn("child", "childClass"), new SimpleListLensDefn<>("a", "double")), lensParser.apply("child/childClass,a/**double"));
        assertEquals(List.of(new ViewLensDefn("child", "childClass"), new SimpleListLensDefn<>("a", "integer")), lensParser.apply("child/childClass,a/**integer"));
        assertEquals(List.of(new ViewLensDefn("child", "childClass"), new SimpleListLensDefn<>("a", "boolean")), lensParser.apply("child/childClass,a/**boolean"));
        assertEquals(List.of(new ViewLensDefn("child", "childClass"), new FirstItemInListDefn<Object>(), new ListLensDefn("a", "address")),
                lensParser.apply("child/childClass,{firstItem},a/*address"));
    }


    @Test public void testLensLineParser() {
        assertEquals(new LensLine("lens1", List.of(new ViewLensDefn("child", "childClass"), new StringLensDefn("line1"))),
                lineParser.apply("lens1=child/childClass,line1/string"));
    }

    @Test public void testLensStoreParser() {
        List<String> lines = List.of("line1=child/childClass,line1/double", "line1=child/childClass,line1/string", "line3=child1/childClass,line1/integer");
        assertEquals(Lists.map(lines, s -> lineParser.apply(s)), storeParser.apply(Lists.join(lines, "\n")).defns);
    }
}