package one.xingyi.core.typeDom;
import one.xingyi.core.codeDom.PackageAndClassName;
import one.xingyi.core.embedded.Embedded;
import one.xingyi.core.names.IClassNameStrategy;
import one.xingyi.core.names.IPackageNameStrategy;
import one.xingyi.core.names.IServerNames;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static one.xingyi.core.utils.Strings.lift;
import static org.junit.Assert.assertEquals;

public class TypeDomTest {
    IServerNames names = IServerNames.simple(IPackageNameStrategy.simple, IClassNameStrategy.simple);
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
        assertEquals(stringPt, TypeDom.create(names, "()" + stringPn.asString()).get());
        assertEquals(intPt, TypeDom.create(names, "()" + intPn.asString()).get());
        assertEquals(doublePt, TypeDom.create(names, "()" + doublePn.asString()).get());
        assertEquals(booleanPt, TypeDom.create(names, "()" + booleanPn.asString()).get());
    }
    @Test public void testList() {
        assertEquals(listStringPt, TypeDom.create(names, "()" + listStringPn.asString()).get());
        assertEquals(listIntPt, TypeDom.create(names, "()" + listIntPn.asString()).get());
        assertEquals(listDoublePt, TypeDom.create(names, "()" + listDoublePn.asString()).get());
        assertEquals(listBooleanPt, TypeDom.create(names, "()" + listBooleanPn.asString()).get());
    }
    @Test public void testEmbedded() {
        assertEquals(embeededStringPt, TypeDom.create(names, "()" + embeddedStringPn.asString()).get());
        assertEquals(embeededIntPt, TypeDom.create(names, "()" + embeddedIntPn.asString()).get());
    }

    @Test public void testView() {
        assertEquals(new ViewType("a.b.IPersonDefn", "a.b.domain.IPerson"), TypeDom.create(names, "a.b.IPersonDefn").get());
    }
    @Test public void testAnythingInsideBracketsDoesntWork() {
        assertEquals(Optional.empty(), TypeDom.create(names, "Thing<something>"));
    }

    @Test public void testTransformed() {
        TypeDom viewType = TypeDom.create(names, "a.b.IPersonDefn").get();
        assertEquals("java.lang.String", stringPt.transformed());
        assertEquals("java.util.List<java.lang.String>", listStringPt.transformed());
        assertEquals("a.b.domain.IPerson", viewType.transformed());
        ListType listType = (ListType) TypeDom.create(names, "java.util.List<a.b.IPersonDefn>").get();
        assertEquals("java.util.List<a.b.domain.IPerson>", listType.transformed());

    }

}