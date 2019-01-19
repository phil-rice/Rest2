package one.xingyi.core.codeDom;
import one.xingyi.core.annotations.Entity;
import one.xingyi.core.names.EntityNames;
import one.xingyi.core.names.IServerNames;
import one.xingyi.core.reflection.Reflection;
import org.junit.Test;

import java.util.Optional;

import static one.xingyi.core.codeDom.EntityDom.create;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
public class EntityDomTest {


    @Entity(entityName = "someEntityName", bookmark = "someBookmark", getUrl = "someUrl")
    class DummyClass {

    }

    Reflection<FieldDomTest> reflection = new Reflection(DummyClass.class);


    @Test public void testCreate() {
        IServerNames names = mock(IServerNames.class);
        String originalEntityName = DummyClass.class.getName();
        EntityNames entityNames = mock(EntityNames.class);
        FieldListDom fieldListDom = mock(FieldListDom.class);


        when(names.entityName(originalEntityName, "someEntityName")).thenReturn(entityNames);
        when(names.bookmark(entityNames, "someBookmark")).thenReturn(Optional.of("theBookmark"));
        when(names.getUrl(entityNames, "someUrl")).thenReturn(Optional.of("theGetUrl"));

        EntityDom actual = create(names, originalEntityName, reflection.classAnnotation(Entity.class), fieldListDom);
        assertEquals(new EntityDom(entityNames, Optional.of("theBookmark"), Optional.of("theGetUrl"), fieldListDom), actual);
    }

}