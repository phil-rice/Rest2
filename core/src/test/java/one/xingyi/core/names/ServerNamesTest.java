package one.xingyi.core.names;
import one.xingyi.core.codeDom.PackageAndClassName;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
public class ServerNamesTest {


    @Test public void testEntityName() {
        //mildly guilty about using real, but I tried with mocks and it wasn't as clear
        IPackageNameStrategy packageNameStrategy = IPackageNameStrategy.simple;
        IClassNameStrategy classNameStrategy = IClassNameStrategy.simple;
        IServerNames names = IServerNames.simple(packageNameStrategy, classNameStrategy);


        EntityNames actual = names.entityName("original.package.class", "EntityRoot");
        assertEquals(new PackageAndClassName("original.package.class"), actual.originalDefn);
        assertEquals(new PackageAndClassName("original.package.domain.EntityRoot"), actual.serverEntity);
        assertEquals(new PackageAndClassName("original.package.server.companion.EntityRootCompanion"), actual.serverCompanion);
        assertEquals(new PackageAndClassName("original.package.client.entitydefn.EntityRootEntity"), actual.clientEntity);
    }
}