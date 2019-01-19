package one.xingyi.core.typeDom;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
@EqualsAndHashCode
@RequiredArgsConstructor
@ToString

public class ViewType implements TypeDom {
    final String typeName;
    final String transformed;
    @Override public String fullTypeName() { return typeName; }
    @Override public String transformed() { return transformed; }
    @Override public TypeDom nested() { return this; }
    @Override public boolean primitive() { return false; }
}
