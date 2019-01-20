package one.xingyi.core.typeDom;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Optional;
@EqualsAndHashCode
@RequiredArgsConstructor
@ToString
public class ViewType implements TypeDom {
    final String typeName;
    final String forEntity;
    final String forView;
    @Override public String fullTypeName() { return typeName; }
    @Override public String forEntity() { return forEntity; }
    @Override public String forView() { return forView; }
    @Override public TypeDom nested() { return this; }
    @Override public boolean primitive() { return false; }
}
