package one.xingyi.core.typeDom;


import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.codeDom.FieldDom;
import one.xingyi.core.codeDom.PackageAndClassName;
import one.xingyi.core.utils.Strings;

@EqualsAndHashCode
@ToString
class StringPrimitiveType extends PrimitiveType {
    public StringPrimitiveType() {
        super(new PackageAndClassName(String.class.getName()));
    }
    @Override public String forToJson(String fieldName, boolean templated) {
        if (templated)
            return "context.template(" + fieldName + ")";
        else
            return fieldName;

    }
    @Override public String makeLens(PackageAndClassName companion, String interfaceName, FieldDom viewDom, String lensName) {
        return "default public Lens<" + interfaceName + "," + viewDom.typeDom.forView() + "> " + viewDom.name + "Lens(){ return xingYi().stringLens(" + companion.asString() + ".companion, " + Strings.quote(lensName) + ");}";
    }
    @Override public String forFromJson(String fieldName) { return "jsonParser.asString(j, " + Strings.quote(fieldName) + ")"; }
    @Override public String fromJsonString() { return "asSimpleStringList"; }
    @Override public String fromJsonStringForSimpleList() { return "simpleStringListLens"; }

}
@EqualsAndHashCode
@ToString
class DoublePrimitiveType extends PrimitiveType {
    public DoublePrimitiveType() {
        super(new PackageAndClassName(Double.class.getName()));
    }
    @Override public String forToJson(String fieldName, boolean templated) { return fieldName; }

    @Override public String makeLens(PackageAndClassName companion, String interfaceName, FieldDom viewDom, String lensName) {
        return "default public Lens<" + interfaceName + "," + viewDom.typeDom.forView() + "> " + viewDom.name + "Lens(){ return xingYi().doubleLens(" + companion.asString() + ".companion, " + Strings.quote(lensName) + ");}";
    }
    @Override public String forFromJson(String fieldName) { return "jsonParser.asDouble(j, " + Strings.quote(fieldName) + ")"; }
    @Override public String fromJsonString() { return "asSimpleDoubleList"; }
    @Override public String fromJsonStringForSimpleList() { return "simpleDoubleListLens"; }

}
@EqualsAndHashCode
@ToString
class BooleanPrimitiveType extends PrimitiveType {
    public BooleanPrimitiveType() {
        super(new PackageAndClassName(Boolean.class.getName()));
    }
    @Override public String forToJson(String fieldName, boolean templated) { return fieldName; }

    @Override public String makeLens(PackageAndClassName companion, String interfaceName, FieldDom viewDom, String lensName) {
        return "default public Lens<" + interfaceName + "," + viewDom.typeDom.forView() + "> " + viewDom.name + "Lens(){ return xingYi().booleanLens(" + companion.asString() + ".companion, " + Strings.quote(lensName) + ");}";
    }
    @Override public String forFromJson(String fieldName) { return "jsonParser.asBoolean(j, " + Strings.quote(fieldName) + ")"; }
    @Override public String fromJsonString() { return "asSimpleBooleanList"; }
    @Override public String fromJsonStringForSimpleList() { return "simpleBooleanListLens"; }
}
@EqualsAndHashCode
@ToString
class IntegerPrimitiveType extends PrimitiveType {
    public IntegerPrimitiveType() {
        super(new PackageAndClassName(Integer.class.getName()));
    }
    @Override public String forToJson(String fieldName, boolean templated) { return fieldName; }

    @Override public String makeLens(PackageAndClassName companion, String interfaceName, FieldDom viewDom, String lensName) {
        return "default public Lens<" + interfaceName + "," + viewDom.typeDom.forView() + "> " + viewDom.name + "Lens(){ return xingYi().integerLens(" + companion.asString() + ".companion, " + Strings.quote(lensName) + ");}";
    }
    @Override public String forFromJson(String fieldName) { return "jsonParser.asInt(j, " + Strings.quote(fieldName) + ")"; }
    @Override public String fromJsonString() { return "asSimpleIntegerList"; }
    @Override public String fromJsonStringForSimpleList() { return "simpleIntegerListLens"; }
}

@EqualsAndHashCode
@RequiredArgsConstructor
@ToString
abstract public class PrimitiveType implements TypeDom {
    final PackageAndClassName typeName;

    public abstract String fromJsonString();
    public abstract String fromJsonStringForSimpleList();

    @Override public String fullTypeName() { return typeName.asString(); }
    @Override public TypeDom nested() { return this; }
    @Override public boolean primitive() { return true; }
    @Override public String entityNameForLens() { return typeName.className; }

    @Override public String lensDefn(String fieldName) { return fieldName + "/" + entityNameForLens(); }
    @Override public boolean isAssignableFrom(TypeDom other) { return other instanceof PrimitiveType && typeName.equals(((PrimitiveType) other).typeName); }

}
