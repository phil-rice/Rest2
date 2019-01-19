package one.xingyi.core.names;
import one.xingyi.core.codeDom.PackageAndClassName;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
public class SimpleServerNamesTest {
    //mildly guilty about using real, but I tried with mocks and it wasn't as clear
    IPackageNameStrategy packageNameStrategy = IPackageNameStrategy.simple;
    IClassNameStrategy classNameStrategy = IClassNameStrategy.simple;
    IServerNames serverNames = IServerNames.simple(packageNameStrategy, classNameStrategy);


    @Test public void testEntityName() {

        EntityNames actual = serverNames.entityName("original.package.class", "EntityRoot");
        assertEquals(new PackageAndClassName("original.package.class"), actual.originalDefn);
        assertEquals(new PackageAndClassName("original.package.domain.EntityRoot"), actual.serverEntity);
        assertEquals(new PackageAndClassName("original.package.server.companion.EntityRootCompanion"), actual.serverCompanion);
        assertEquals(new PackageAndClassName("original.package.client.entitydefn.EntityRootEntity"), actual.clientEntity);
    }

    @Test public void testViewName() {
        ViewNames actual = serverNames.viewName("original.package.class", "ViewRoot");
        assertEquals(new PackageAndClassName("original.package.class"), actual.originalDefn);
        assertEquals(new PackageAndClassName("original.package.client.view.ViewRoot"), actual.clientView);
        assertEquals(new PackageAndClassName("original.package.client.companion.ViewRootCompanion"), actual.clientCompanion);
    }

    @Test public void testLensName() {
        EntityNames entityNames = new EntityNames(null, new PackageAndClassName("something.someclass"), null, null);
        assertEquals("lens_someclass_fieldName", serverNames.entityLensName(entityNames, "fieldName", ""));
        assertEquals("override", serverNames.entityLensName(entityNames, "fieldName", "override"));
       }
    @Test public void testLensPath() {
        EntityNames entityNames = mock(EntityNames.class);
        assertEquals("fieldName", serverNames.entityLensPath(entityNames, "fieldName", ""));
        assertEquals("override", serverNames.entityLensPath(entityNames, "fieldName", "override"));
    }
    @Test public void testBookmark() {
        EntityNames entityNames = mock(EntityNames.class);
        assertEquals(Optional.empty(), serverNames.bookmark(entityNames, ""));
        assertEquals(Optional.of("override"), serverNames.bookmark(entityNames, "override"));
    }
    @Test public void testGetUrl() {
        EntityNames entityNames = mock(EntityNames.class);
        assertEquals(Optional.empty(), serverNames.getUrl(entityNames, ""));
        assertEquals(Optional.of("override"), serverNames.getUrl(entityNames, "override"));
    }
}
