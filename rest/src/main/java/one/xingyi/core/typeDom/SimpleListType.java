package one.xingyi.core.typeDom;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.codeDom.FieldDom;
import one.xingyi.core.codeDom.PackageAndClassName;
import one.xingyi.core.utils.Strings;

@RequiredArgsConstructor @ToString @EqualsAndHashCode
public class SimpleListType implements TypeDom {
    final String fullTypeName;
    final PrimitiveType nested;
    @Override public String fullTypeName() { return fullTypeName; }
    @Override public TypeDom nested() { return nested; }
    @Override public boolean primitive() { return false; }
    @Override public String entityNameForLens() { return nested.entityNameForLens(); }
    @Override public String makeLens(PackageAndClassName companion, String interfaceName, FieldDom viewDom, String lensName) {
        return "default public Lens<" + interfaceName + "," + viewDom.typeDom.forView() + ">" +
                viewDom.name + "Lens(){return xingYi().simpleListLens(" + companion.asString() + ".companion, " + Strings.quote(lensName) + ");}";
    }

    @Override public String forToJson(String fieldName, boolean templated) { return "jsonWriter.makeSimpleList(" + fieldName + ")"; }
    @Override public String forFromJson(String fieldName) { return "jsonParser." + nested.fromJsonString() + "(j," + Strings.quote(fieldName) + ")"; }


    @Override public String lensDefn(String fieldName) { return fieldName + "/**" + entityNameForLens(); }
    @Override public boolean isAssignableFrom(TypeDom other) { return other instanceof SimpleListType && nested.fullTypeName().equals(other.nested().fullTypeName()); }

}
