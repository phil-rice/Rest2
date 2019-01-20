package one.xingyi.core.typeDom;
import one.xingyi.core.codeDom.PackageAndClassName;
import one.xingyi.core.embedded.Embedded;
import one.xingyi.core.names.IClassNameStrategy;
import one.xingyi.core.names.IPackageNameStrategy;
import one.xingyi.core.names.IServerNames;
import one.xingyi.core.validation.Result;
import org.junit.Test;

import java.util.List;

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
        assertEquals(Result.succeed(stringPt), TypeDom.create(names, "()" + stringPn.asString()));
        assertEquals(Result.succeed(intPt), TypeDom.create(names, "()" + intPn.asString()));
        assertEquals(Result.succeed(doublePt), TypeDom.create(names, "()" + doublePn.asString()));
        assertEquals(Result.succeed(booleanPt), TypeDom.create(names, "()" + booleanPn.asString()));
    }
    @Test public void testList() {
        assertEquals(Result.succeed(listStringPt), TypeDom.create(names, "()" + listStringPn.asString()));
        assertEquals(Result.succeed(listIntPt), TypeDom.create(names, "()" + listIntPn.asString()));
        assertEquals(Result.succeed(listDoublePt), TypeDom.create(names, "()" + listDoublePn.asString()));
        assertEquals(Result.succeed(listBooleanPt), TypeDom.create(names, "()" + listBooleanPn.asString()));
    }
    @Test public void testEmbedded() {
        assertEquals(Result.succeed(embeededStringPt), TypeDom.create(names, "()" + embeddedStringPn.asString()));
        assertEquals(Result.succeed(embeededIntPt), TypeDom.create(names, "()" + embeddedIntPn.asString()));
    }

    @Test public void testView() {
        Result<String, TypeDom> actual = TypeDom.create(names, "a.b.IPersonDefn");
        assertEquals(Result.succeed(new ViewType("a.b.IPersonDefn", "a.b.domain.IPerson", "a.b.client.view.Person", "")), actual);
    }
    @Test public void testAnythingInsideBracketsDoesntWork() {
        assertEquals(Result.failwith("Could not work out what type Thing<something> was"), TypeDom.create(names, "Thing<something>"));
    }

    @Test public void testTransformed() {
        assertEquals("java.lang.String", stringPt.forEntity());
        assertEquals("java.util.List<java.lang.String>", listStringPt.forEntity());
        TypeDom viewType = TypeDom.create(names, "a.b.IPersonDefn").result().get();
        assertEquals("a.b.domain.IPerson", viewType.forEntity());
        assertEquals("a.b.client.view.Person", viewType.forView());

        TypeDom listType = TypeDom.create(names, "java.util.List<a.b.IPersonDefn>").result().get();
        assertEquals("java.util.List<a.b.domain.IPerson>", listType.forEntity());
        assertEquals("java.util.List<a.b.IPersonDefn>", listType.forView());

    }

}