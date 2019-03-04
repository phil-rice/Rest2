package one.xingyi.core.typeDom;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.codeDom.FieldDom;
import one.xingyi.core.codeDom.PackageAndClassName;
import one.xingyi.core.utils.Strings;

@RequiredArgsConstructor @ToString @EqualsAndHashCode
public class SimpleListType  {
    final String fullTypeName;
    final TypeDom nested;
//    @Override public String fullTypeName() { return fullTypeName; }
//    @Override public TypeDom nested() { return nested; }
//    @Override public boolean primitive() { return false; }
//    @Override public String entityNameForLens() {
//        return null;
//    }
//    @Override public String makeLens(PackageAndClassName companion, String interfaceName, FieldDom viewDom, String lensName) {
//        return null;
//    }
//    @Override public String forFromJson(String fieldName) {
//        return null;
//    }
//
//    @Override public String forToJson(String fieldName, boolean templated) {
//        if (templated && typeName.className.equalsIgnoreCase("String"))
//            return "context.template(" + fieldName + ")";
//        else
//            return fieldName;
//
//    }
//    //TODO look at this and the above method. Need to move strings to their own type
//    @Override public String forFromJson(String fieldName) {
//        if (typeName.className.equalsIgnoreCase("String"))
//            return "jsonParser.asString(j, " + Strings.quote(fieldName) + ")";
//        if (typeName.className.equalsIgnoreCase("Integer"))
//            return "jsonParser.asInt(j, " + Strings.quote(fieldName) + ")";
//        else
//            throw new RuntimeException("Don't know how to parse primitive field " + fieldName + " of type" + typeName.asString());
//    }
//
//
//    @Override public String lensDefn(String fieldName) { return fieldName + "/" + entityNameForLens(); }
//    @Override public boolean isAssignableFrom(TypeDom other) { return other instanceof SimpleListType && nested.fullTypeName().equals(other.nested().fullTypeName()); }

}
