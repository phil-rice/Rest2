package one.xingyi.core.typeDom;
import one.xingyi.core.annotationProcessors.ViewDefnNameToViewNamesFixture;
import one.xingyi.core.client.IResourceList;
import one.xingyi.core.codeDom.PackageAndClassName;
import one.xingyi.core.embedded.Embedded;
import one.xingyi.core.names.IClassNameStrategy;
import one.xingyi.core.names.IPackageNameStrategy;
import one.xingyi.core.names.IServerNames;
import one.xingyi.core.validation.Result;
import org.junit.Test;

import java.util.Map;

import static junit.framework.TestCase.fail;
import static one.xingyi.core.utils.Strings.lift;
import static org.junit.Assert.assertEquals;

public class TypeDomTest implements ViewDefnNameToViewNamesFixture {
    IServerNames names = IServerNames.simple(IPackageNameStrategy.simple, IClassNameStrategy.simple);
    PackageAndClassName pn(String s) {return new PackageAndClassName(s);}

    PackageAndClassName stringPn = pn(String.class.getName());
    PrimitiveType stringPt = new PrimitiveType(stringPn);

//    PackageAndClassName listStringPn = pn(lift(ISimpleList.class.getName(), String.class.getName()));
//    ListType listStringPt = new ListType(listStringPn.asString(), stringPt,new PackageAndClassName("stringCompanion name... doesn't work"));

    PackageAndClassName embeddedStringPn = pn(lift(Embedded.class.getName(), String.class.getName()));
    EmbeddedType embeededStringPt = new EmbeddedType(embeddedStringPn.asString(), stringPt);

    PackageAndClassName intPn = pn(Integer.class.getName());
    PackageAndClassName listIntPn = pn(lift(IResourceList.class.getName(), Integer.class.getName()));
    PrimitiveType intPt = new PrimitiveType(intPn);
    //    ListType listIntPt = new ListType(listIntPn.asString(), intPt);
    PackageAndClassName embeddedIntPn = pn(lift(Embedded.class.getName(), Integer.class.getName()));
    EmbeddedType embeededIntPt = new EmbeddedType(embeddedIntPn.asString(), intPt);

    PackageAndClassName doublePn = pn(Double.class.getName());
    PackageAndClassName listDoublePn = pn(lift(IResourceList.class.getName(), Double.class.getName()));
    PrimitiveType doublePt = new PrimitiveType(doublePn);
//    ListType listDoublePt = new ListType(listDoublePn.asString(), doublePt);

    PackageAndClassName booleanPn = pn(Boolean.class.getName());
    PackageAndClassName listBooleanPn = pn(lift(IResourceList.class.getName(), Boolean.class.getName()));
    PrimitiveType booleanPt = new PrimitiveType(booleanPn);
//    ListType listBooleanPt = new ListType(listBooleanPn.asString(), booleanPt);

    @Test public void testPrimitives() {
        assertEquals(Result.succeed(stringPt), TypeDom.create(names, "()" + stringPn.asString(), viewToViewNames));
        assertEquals(Result.succeed(intPt), TypeDom.create(names, "()" + intPn.asString(), viewToViewNames));
        assertEquals(Result.succeed(doublePt), TypeDom.create(names, "()" + doublePn.asString(), viewToViewNames));
        assertEquals(Result.succeed(booleanPt), TypeDom.create(names, "()" + booleanPn.asString(), viewToViewNames));
    }
    //    @Test public void testList() {
//        assertEquals(Result.succeed(listStringPt), TypeDom.create(names, "()" + listStringPn.asString()));
//        assertEquals(Result.succeed(listIntPt), TypeDom.create(names, "()" + listIntPn.asString()));
//        assertEquals(Result.succeed(listDoublePt), TypeDom.create(names, "()" + listDoublePn.asString()));
//        assertEquals(Result.succeed(listBooleanPt), TypeDom.create(names, "()" + listBooleanPn.asString()));
//    }
    @Test public void testEmbedded() {
        assertEquals(Result.succeed(embeededStringPt), TypeDom.create(names, "()" + embeddedStringPn.asString(), viewToViewNames));
        assertEquals(Result.succeed(embeededIntPt), TypeDom.create(names, "()" + embeddedIntPn.asString(), viewToViewNames));
    }

    @Test public void testView() {
//        fail();
        Result<String, TypeDom> actual = TypeDom.create(names, "a.b.IPersonViewDefn", viewToViewNames);
        assertEquals(Result.succeed(
                new ViewType("a.b.IPersonViewDefn", "a.b.server.domain.IPerson",
                        "a.b.client.view.PersonView", "a.b.client.viewcompanion.PersonViewCompanion",
                        "a.b.server.companion.PersonCompanion", "Person")), actual);
    }
    @Test public void testAnythingInsideBracketsDoesntWork() {
        assertEquals(Result.failwith("Could not work out what type Thing<something> was. Known views are\n" +
                "IOneViewDefn\n" +
                "IPersonViewDefn\n" +
                "ITwoViewDefn"), TypeDom.create(names, "Thing<something>", viewToViewNames));
    }

    @Test public void testTransformed() {
        assertEquals("java.lang.String", stringPt.forEntity());
//        assertEquals("one.xingyi.core.client.ISimpleList<java.lang.String>", listStringPt.forEntity());
        Result<String, TypeDom> result = TypeDom.create(names, "a.b.IPersonViewDefn", viewToViewNames);
        TypeDom viewType = result.result().get();

        assertEquals(result.toString(), "a.b.server.domain.IPerson", viewType.forEntity());
        assertEquals(result.toString(), "a.b.client.view.PersonView", viewType.forView());

        Result<String, TypeDom> result1 = TypeDom.create(names, "one.xingyi.core.client.IResourceList<a.b.IPersonViewDefn>", viewToViewNames);
        System.out.println(result1);
        TypeDom listType = result1.result().get();
        assertEquals("one.xingyi.core.client.IResourceList<a.b.server.domain.IPerson>", listType.forEntity());
        assertEquals("one.xingyi.core.client.IResourceList<a.b.client.view.PersonView>", listType.forView());

    }

}