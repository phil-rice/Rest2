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

    @Test public void testLensValueParser() {
        assertEquals(List.of(new ObjectLens("child", "childClass"), new StringLensDefn("line1", "string")), lensParser.apply("child/childClass,line1/string"));
        assertEquals(List.of(new ObjectLens("child", "childClass"), new StringLensDefn("line1", "integer")), lensParser.apply("child/childClass,line1/integer"));
        assertEquals(List.of(new ObjectLens("child", "childClass"), new StringLensDefn("line1", "double")), lensParser.apply("child/childClass,line1/double"));
    }

    @Test public void testLensLineParser() {
        assertEquals(new LensLine("lens1", List.of(new ObjectLens("child", "childClass"), new StringLensDefn("line1", "string"))),
                lineParser.apply("lens1=child/childClass,line1/string"));
    }

    @Test public void testLensStoreParser() {
        List<String> lines = List.of("line1=child/childClass,line1/double", "line1=child/childClass,line1/string", "line3=child1/childClass,line1/integer");
        assertEquals(Lists.map(lines, s -> lineParser.apply(s)), storeParser.apply(Lists.join(lines, "\n")).defns);
    }
}