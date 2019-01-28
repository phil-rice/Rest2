package one.xingyi.core.typeDom;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;
@EqualsAndHashCode
@RequiredArgsConstructor
@ToString

public class ListType implements TypeDom {
    final String fullTypeName;
    final TypeDom nested;
    @Override public String forEntity() { return List.class.getName() + "<" + nested.forEntity() + ">"; }
    @Override public String fullTypeName() { return fullTypeName; }
    @Override public boolean primitive() { return false; }
    @Override public String forFromJson(String fieldName) {
        return null;//TODO
    }
    @Override public TypeDom nested() { return nested; }

}
