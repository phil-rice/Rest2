package one.xingyi.core.typeDom;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.utils.Strings;
@EqualsAndHashCode
@RequiredArgsConstructor
@ToString

public class EmbeddedType implements NonPrimitiveTypeDom {
    final String fullTypeName;
    final TypeDom nested;

    @Override public String fullTypeName() {
        return fullTypeName;
    }
    @Override public TypeDom nested() { return nested; }
    @Override public boolean primitive() { return false; }
    @Override public String entityNameForLens() { return nested.entityNameForLens(); }
    @Override public String forFromJson(String fieldName) {
        return null; //TODO
    }
    @Override public String lensDefn(String fieldName) { return "fieldName/embedded-" + entityNameForLens(); }

}
