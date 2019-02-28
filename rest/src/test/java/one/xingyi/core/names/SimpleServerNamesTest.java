package one.xingyi.core.names;
import one.xingyi.core.codeDom.PackageAndClassName;
import one.xingyi.core.endpoints.BookmarkCodeAndUrlPattern;
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
        EntityNames actual = serverNames.entityName("original.package.IEntityDefn");
        assertEquals(new PackageAndClassName("original.package.IEntityDefn"), actual.originalDefn);
        assertEquals(new PackageAndClassName("original.package.server.domain.IEntity"), actual.serverInterface);
        assertEquals(new PackageAndClassName("original.package.server.domain.Entity"), actual.serverEntity);
        assertEquals(new PackageAndClassName("original.package.server.companion.EntityCompanion"), actual.serverCompanion);
        assertEquals(new PackageAndClassName("original.package.server.controller.IEntityController"), actual.serverController);
        assertEquals(new PackageAndClassName("original.package.client.entitydefn.IEntityClientEntity"), actual.clientResource);
        assertEquals(new PackageAndClassName("original.package.client.resourcecompanion.EntityCompositeCompanion"), actual.clientResourceCompanion);
        assertEquals("Entity", actual.entityNameForLens);
    }


    @Test public void testViewName() {
        ViewNames actual = serverNames.viewName("original.package.IViewRootDefn", " a.b.c.IEntityDefn");
        assertEquals(new PackageAndClassName("original.package.IViewRootDefn"), actual.originalDefn);
        assertEquals(new PackageAndClassName("original.package.client.view.ViewRoot"), actual.clientView);
//        assertEquals(new PackageAndClassName("original.package.client.companion.ViewRootCompanion"), actual.clientCompositeCompanion);
    }

    @Test public void testLensName() {
        EntityNames entityNames = new EntityNames(new PackageAndClassName("p.ISomeDefn"), null, null, null, null, null, null, "someEntityForLens");
        assertEquals("lens_someEntityForLens_fieldName", serverNames.entityLensName(entityNames, "fieldName", ""));
        assertEquals("override", serverNames.entityLensName(entityNames, "fieldName", "override"));
    }
    @Test public void testLensPath() {
        EntityNames entityNames = mock(EntityNames.class);
        assertEquals(Result.succeed("fieldName"), serverNames.entityLensPath(entityNames, "fieldName", ""));
        assertEquals(Result.succeed("name1"), serverNames.entityLensPath(entityNames, "fieldName", "name1"));
    }
    @Test public void testBookmark() {
        EntityNames entityNames = mock(EntityNames.class);
        assertEquals(Optional.empty(), serverNames.bookmarkAndUrl(entityNames, "", "", ""));
        assertEquals(Optional.empty(), serverNames.bookmarkAndUrl(entityNames, "just bookmark", "", ""));
        assertEquals(Optional.empty(), serverNames.bookmarkAndUrl(entityNames, "", "justget", ""));
        assertEquals(Optional.of(new BookmarkCodeAndUrlPattern("overrideB", "overrideU", "{host}overrideB/code")),
                serverNames.bookmarkAndUrl(entityNames, "overrideB", "overrideU", ""));
        assertEquals(Optional.of(new BookmarkCodeAndUrlPattern("overrideB", "overrideU", "overrideC")),
                serverNames.bookmarkAndUrl(entityNames, "overrideB", "overrideU", "overrideC"));
    }
}
