package one.xingyi.core.codeDom;
import one.xingyi.core.annotations.Field;
import one.xingyi.core.reflection.Reflection;
import one.xingyi.core.typeDom.TypeDom;
import org.junit.Test;

import java.lang.ref.Reference;
import java.util.Optional;

import static one.xingyi.core.codeDom.FieldDom.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
public class FieldDomTest {

    @Field(lensName = "someLensName", lensPath = "someLensPath", readOnly = true)
    public String readOnlyField() {return null;}
    @Field(lensName = "someLensName", lensPath = "someLensPath", readOnly = false)
    public String readWriteField() {return null;}

    Reflection<FieldDomTest> reflection = new Reflection(getClass());


    @Test public void testCreate() {
        INames names = mock(INames.class);
        TypeDom typeDom = mock(TypeDom.class);
        PackageAndClassName pCName = mock(PackageAndClassName.class);
        when(names.getUrl(pCName, "")).thenReturn(Optional.of("theGetUrl"));
        when(names.bookmark(pCName, "")).thenReturn(Optional.of("theBookmark"));
        when(names.lensName(pCName, "someFieldName","someLensName")).thenReturn("theLensName");
        when(names.lensPath(pCName, "someFieldName","someLensPath")).thenReturn("theLensPath");

        FieldDom actualReadOnly = create(names, pCName, typeDom, "someFieldName", reflection.methodAnnotation(Field.class, "readOnlyField"));
        assertEquals(new FieldDom(typeDom, "someFieldName", true, "theLensName", "theLensPath", Optional.empty()), actualReadOnly);

        FieldDom actualReadWrite = create(names, pCName, typeDom, "someFieldName", reflection.methodAnnotation(Field.class, "readWriteField"));
        assertEquals(new FieldDom(typeDom, "someFieldName", false, "theLensName", "theLensPath", Optional.empty()), actualReadWrite);
    }

}