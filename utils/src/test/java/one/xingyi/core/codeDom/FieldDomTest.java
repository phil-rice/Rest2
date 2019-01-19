package one.xingyi.core.codeDom;
import one.xingyi.core.annotations.Field;
import one.xingyi.core.names.EntityNames;
import one.xingyi.core.names.IServerNames;
import one.xingyi.core.reflection.Reflection;
import one.xingyi.core.typeDom.TypeDom;
import org.junit.Test;

import java.util.Optional;

import static one.xingyi.core.codeDom.FieldDom.create;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
public class FieldDomTest {

    @Field(lensName = "someLensName", lensPath = "someLensPath", readOnly = true)
    public String readOnlyField() {return null;}
    @Field(lensName = "someLensName", lensPath = "someLensPath", readOnly = false)
    public String readWriteField() {return null;}

    Reflection<FieldDomTest> reflection = new Reflection(getClass());


    @Test public void testCreate() {
        IServerNames names = mock(IServerNames.class);
        TypeDom typeDom = mock(TypeDom.class);
        EntityNames entityNames = mock(EntityNames.class);
        when(names.entityLensName(entityNames, "someFieldName","someLensName")).thenReturn("theLensName");
        when(names.entityLensPath(entityNames, "someFieldName","someLensPath")).thenReturn("theLensPath");

        FieldDom actualReadOnly = create(names, entityNames, typeDom, "someFieldName", reflection.methodAnnotation(Field.class, "readOnlyField"));
        assertEquals(new FieldDom(typeDom, "someFieldName", true, "theLensName", "theLensPath", Optional.empty()), actualReadOnly);

        FieldDom actualReadWrite = create(names, entityNames, typeDom, "someFieldName", reflection.methodAnnotation(Field.class, "readWriteField"));
        assertEquals(new FieldDom(typeDom, "someFieldName", false, "theLensName", "theLensPath", Optional.empty()), actualReadWrite);
    }

}