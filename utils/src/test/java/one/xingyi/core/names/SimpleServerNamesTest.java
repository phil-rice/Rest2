package one.xingyi.core.names;
import one.xingyi.core.codeDom.PackageAndClassName;
import one.xingyi.core.endpoints.BookmarkAndUrlPattern;
import one.xingyi.core.validation.Result;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
public class SimpleServerNamesTest {
    //mildly guilty about using real, but I tried with mocks and it wasn't as clear
    //There is a lot of repetition in the tests, but this is a place where mistakes are easy and this helps catch them early
    IPackageNameStrategy packageNameStrategy = IPackageNameStrategy.simple;
    IClassNameStrategy classNameStrategy = IClassNameStrategy.simple;
    IServerNames serverNames = IServerNames.simple(packageNameStrategy, classNameStrategy);


    @Test public void testEntityNameWhenClassStartsWithIAndEndInDefn() {
        EntityNames actual = serverNames.entityName("original.package.IEntityDefn").result().get();
        assertEquals(new PackageAndClassName("original.package.IEntityDefn"), actual.originalDefn);
        assertEquals(new PackageAndClassName("original.package.domain.IEntity"), actual.serverInterface);
        assertEquals(new PackageAndClassName("original.package.domain.Entity"), actual.serverEntity);
        assertEquals(new PackageAndClassName("original.package.server.companion.EntityCompanion"), actual.serverCompanion);
        assertEquals(new PackageAndClassName("original.package.client.entitydefn.IEntityClientEntity"), actual.clientEntity);
        assertEquals("Entity", actual.entityNameForLens);
    }

    @Test public void testEntityNameWhenRootDoesntStartWithI() {
        assertEquals(List.of("Entity [EntityDefn] Should start with an I"), serverNames.entityName("original.package.EntityDefn").fails());
    }


    @Test public void testViewName() {
        Result<String, ViewNames> actual = serverNames.viewName("original.package.IViewRootDefn", " a.b.c.IEntityDefn");
        assertEquals(new PackageAndClassName("original.package.IViewRootDefn"), actual.result().get().originalDefn);
        assertEquals(new PackageAndClassName("original.package.client.view.ViewRoot"), actual.result().get().clientView);
//        assertEquals(new PackageAndClassName("original.package.client.companion.ViewRootCompanion"), actual.clientCompanion);
    }
    @Test public void testViewNameWithoutAnI() {
        assertEquals(List.of("View [class] Should start with an I", "View [class] Should end with Defn"),
                serverNames.viewName("original.package.class", "EntityClassName").fails());
    }

    @Test public void testLensName() {
        EntityNames entityNames = new EntityNames(new PackageAndClassName("p.ISomeDefn"), null, null, null, null, "someEntityForLens");
        assertEquals("lens_someEntityForLens_fieldName", serverNames.entityLensName(entityNames, "fieldName", ""));
        assertEquals("override", serverNames.entityLensName(entityNames, "fieldName", "override"));
    }
    @Test public void testLensPath() {
        EntityNames entityNames = mock(EntityNames.class);
        assertEquals("fieldName", serverNames.entityLensPath(entityNames, "fieldName", ""));
        assertEquals("override", serverNames.entityLensPath(entityNames, "fieldName", "override"));
    }
    @Test public void testBookmark() {
        EntityNames entityNames = mock(EntityNames.class);
        assertEquals(Optional.empty(), serverNames.bookmarkAndUrl(entityNames, "", ""));
        assertEquals(Optional.of(new BookmarkAndUrlPattern("overrideB", "overrideU")), serverNames.bookmarkAndUrl(entityNames, "overrideB", "overrideU"));
    }
}
