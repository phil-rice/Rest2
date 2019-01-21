package one.xingyi.core.typeDom;


import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.codeDom.PackageAndClassName;
@EqualsAndHashCode
@RequiredArgsConstructor
@ToString

public class PrimitiveType implements TypeDom {
    final PackageAndClassName typeName;

    @Override public String fullTypeName() { return typeName.asString(); }
    @Override public TypeDom nested() { return this; }
    @Override public boolean primitive() { return true; }
    @Override public String forJson(String fieldName, boolean templated) {
        if (templated && typeName.className.equalsIgnoreCase("String"))
            return "context.template(" + fieldName + ")";
        else
            return fieldName;

    }
}
