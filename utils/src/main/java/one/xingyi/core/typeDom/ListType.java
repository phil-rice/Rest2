package one.xingyi.core.typeDom;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
@EqualsAndHashCode
@RequiredArgsConstructor
@ToString

public class ListType implements TypeDom {
    final String fullTypeName;
    final TypeDom nested;

    @Override public String fullTypeName() { return fullTypeName; }
    @Override public boolean primitive() { return false; }
    @Override public TypeDom nested() { return nested; }

}
