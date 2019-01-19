package one.xingyi.core.codeDom;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.annotations.Field;
import one.xingyi.core.names.EntityNames;
import one.xingyi.core.names.IServerNames;
import one.xingyi.core.typeDom.TypeDom;
import one.xingyi.core.utils.Strings;

import java.util.Optional;
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class FieldDom {
    final TypeDom typeDom;
    final String name;
    final boolean readOnly;
    final String lensName;
    final String lensPath;
    final Optional<String> javascript;

    public static FieldDom create(IServerNames names, EntityNames entityName, TypeDom typeDom, String fieldName, Field field) {
        return new FieldDom(typeDom, fieldName, field.readOnly(),
                names.entityLensName(entityName, fieldName, field.lensName()),
                names.entityLensPath(entityName, fieldName, field.lensPath()),
                Strings.from(field.javascript()));
    }
}
