package one.xingyi.core.typeDom;


import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.codeDom.PackageAndClassName;
import one.xingyi.core.utils.Strings;
@EqualsAndHashCode
@RequiredArgsConstructor
@ToString

public class PrimitiveType implements TypeDom {
    final PackageAndClassName typeName;

    @Override public String fullTypeName() { return typeName.asString(); }
    @Override public TypeDom nested() { return this; }
    @Override public boolean primitive() { return true; }
    @Override public String entityNameForLens() { return typeName.className; }
    @Override public String forToJson(String fieldName, boolean templated) {
        if (templated && typeName.className.equalsIgnoreCase("String"))
            return "context.template(" + fieldName + ")";
        else
            return fieldName;

    }
    //TODO look at this and the above method. Need to move strings to their own type
    @Override public String forFromJson(String fieldName) {
        if (typeName.className.equalsIgnoreCase("String"))
            return "jsonParser.asString(j, " + Strings.quote(fieldName) + ")";
        if (typeName.className.equalsIgnoreCase("Integer"))
            return "jsonParser.asInt(j, " + Strings.quote(fieldName) + ")";
        else throw new RuntimeException("Don't know how to parse primitive field " + fieldName + " of type" + typeName.asString());
    }
    @Override public String lensDefn(String fieldName) { return fieldName + "/" + entityNameForLens(); }

    //TODO Split PrimitiveType
}
