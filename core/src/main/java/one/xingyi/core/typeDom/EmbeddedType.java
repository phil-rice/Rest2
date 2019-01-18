package one.xingyi.core.typeDom;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.codeDom.PackageAndClassName;
@EqualsAndHashCode
@RequiredArgsConstructor
@ToString

public class EmbeddedType implements TypeDom {
    final String fullTypeName;
    final TypeDom nested;

    @Override public String fullTypeName() {
        return fullTypeName;
    }
    @Override public TypeDom nested() { return nested; }
    @Override public boolean primitive() { return false; }
}
