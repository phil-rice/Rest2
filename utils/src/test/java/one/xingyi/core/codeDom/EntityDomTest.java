package one.xingyi.core.codeDom;
import one.xingyi.core.annotations.Entity;
import one.xingyi.core.names.EntityNames;
import one.xingyi.core.names.IServerNames;
import one.xingyi.core.reflection.Reflection;
import one.xingyi.core.validation.Result;
import org.junit.Test;

import java.util.Optional;

import static one.xingyi.core.codeDom.EntityDom.create;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
public class EntityDomTest {

    @Entity(entityName = "someEntityName", bookmark = "someBookmark", getUrl = "someUrl")
    class DummyClass {
    }

    Reflection<DummyClass> reflection = new Reflection(DummyClass.class);

    @Test public void testCreateHappyPath() {
        IServerNames names = mock(IServerNames.class);
        String originalEntityName = DummyClass.class.getName();
        EntityNames entityNames = mock(EntityNames.class);
        FieldListDom fieldListDom = mock(FieldListDom.class);


//        when(names.entityName(originalEntityName, "someEntityName")).thenReturn(Result.succeed(entityNames));
//        when(names.bookmarkAndUrl(entityNames, "someBookmark")).thenReturn(Optional.of("theBookmark"));
//        when(names.getUrl(entityNames, "someUrl")).thenReturn(Optional.of("theGetUrl"));
//
//        Result<String, EntityDom> actual = create(names, originalEntityName, reflection.classAnnotation(Entity.class), fieldListDom);
//        assertEquals(new EntityDom(entityNames, Optional.of("theBookmark"), Optional.of("theGetUrl"), fieldListDom), actual.result().get()
        fail();
    }

}