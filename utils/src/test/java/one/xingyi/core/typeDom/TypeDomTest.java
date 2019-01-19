package one.xingyi.core.typeDom;
import one.xingyi.core.codeDom.PackageAndClassName;
import one.xingyi.core.embedded.Embedded;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static one.xingyi.core.utils.Strings.lift;
import static org.junit.Assert.assertEquals;

public class TypeDomTest {

    PackageAndClassName pn(String s) {return new PackageAndClassName(s);}

    PackageAndClassName stringPn = pn(String.class.getName());
    PrimitiveType stringPt = new PrimitiveType(stringPn);

    PackageAndClassName listStringPn = pn(lift(List.class.getName(), String.class.getName()));
    ListType listStringPt = new ListType(listStringPn.asString(), stringPt);

    PackageAndClassName embeddedStringPn = pn(lift(Embedded.class.getName(), String.class.getName()));
    EmbeddedType embeededStringPt = new EmbeddedType(embeddedStringPn.asString(), stringPt);

    PackageAndClassName intPn = pn(Integer.class.getName());
    PackageAndClassName listIntPn = pn(lift(List.class.getName(), Integer.class.getName()));
    PrimitiveType intPt = new PrimitiveType(intPn);
    ListType listIntPt = new ListType(listIntPn.asString(), intPt);
    PackageAndClassName embeddedIntPn = pn(lift(Embedded.class.getName(), Integer.class.getName()));
    EmbeddedType embeededIntPt = new EmbeddedType(embeddedIntPn.asString(), intPt);

    PackageAndClassName doublePn = pn(Double.class.getName());
    PackageAndClassName listDoublePn = pn(lift(List.class.getName(), Double.class.getName()));
    PrimitiveType doublePt = new PrimitiveType(doublePn);
    ListType listDoublePt = new ListType(listDoublePn.asString(), doublePt);

    PackageAndClassName booleanPn = pn(Boolean.class.getName());
    PackageAndClassName listBooleanPn = pn(lift(List.class.getName(), Boolean.class.getName()));
    PrimitiveType booleanPt = new PrimitiveType(booleanPn);
    ListType listBooleanPt = new ListType(listBooleanPn.asString(), booleanPt);

    @Test public void testPrimitives() {
        assertEquals(stringPt, TypeDom.create("()" +stringPn.asString()).get());
        assertEquals(intPt, TypeDom.create("()" +intPn.asString()).get());
        assertEquals(doublePt, TypeDom.create("()" +doublePn.asString()).get());
        assertEquals(booleanPt, TypeDom.create("()" +booleanPn.asString()).get());
    }
    @Test public void testList() {
        assertEquals(listStringPt, TypeDom.create("()" +listStringPn.asString()).get());
        assertEquals(listIntPt, TypeDom.create("()" +listIntPn.asString()).get());
        assertEquals(listDoublePt, TypeDom.create("()" +listDoublePn.asString()).get());
        assertEquals(listBooleanPt, TypeDom.create("()" +listBooleanPn.asString()).get());
    }
    @Test public void testEmbedded() {
        assertEquals(embeededStringPt, TypeDom.create("()" +embeddedStringPn.asString()).get());
        assertEquals(embeededIntPt, TypeDom.create("()" +embeddedIntPn.asString()).get());
    }

    @Test public void testView() {
        assertEquals(new ViewType("something"), TypeDom.create("something").get());
    }
    @Test public void testAnythingInsideBracketsDoesntWork() {
        assertEquals(Optional.empty(), TypeDom.create("Thing<something>"));
    }

}