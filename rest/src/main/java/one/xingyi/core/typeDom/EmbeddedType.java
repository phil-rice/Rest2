package one.xingyi.core.typeDom;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.codeDom.FieldDom;
import one.xingyi.core.codeDom.PackageAndClassName;
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
    @Override public String makeLens(PackageAndClassName companion, String interfaceName, FieldDom viewDom, String lensName) { return "Cannot handle Embedded yet"; }
    @Override public String forFromJson(String fieldName) {
        return null; //TODO
    }
    @Override public String lensDefn(String fieldName) { return "fieldName/embedded-" + entityNameForLens(); }
    @Override public boolean isAssignableFrom(TypeDom other) { return other.getClass().equals(getClass()) && nested().isAssignableFrom(other.nested()); }

}
