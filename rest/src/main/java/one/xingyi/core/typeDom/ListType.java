package one.xingyi.core.typeDom;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.client.IResourceList;
import one.xingyi.core.codeDom.FieldDom;
import one.xingyi.core.codeDom.PackageAndClassName;
import one.xingyi.core.utils.Strings;

import java.util.List;
@EqualsAndHashCode
@RequiredArgsConstructor
@ToString

public class ListType implements NonPrimitiveTypeDom {
    final String fullTypeName;
    final TypeDom nested;
    final PackageAndClassName companionName;
    final String entityNameForLens;
    @Override public String forEntity() { return IResourceList.class.getName() + "<" + nested.forEntity() + ">"; }
    @Override public String forView() { return IResourceList.class.getName() + "<" + nested.forView() + ">"; }
    @Override public String fullTypeName() { return fullTypeName; }
    @Override public boolean primitive() { return false; }
    @Override public String entityNameForLens() { return nested.entityNameForLens(); }
    @Override public String makeLens(PackageAndClassName companion, String interfaceName, FieldDom viewDom, String lensName) {
        return "default public Lens<" + interfaceName + "," + viewDom.typeDom.forView() + ">" +
                viewDom.name + "Lens(){return xingYi().listLens(" + companion.asString() + ".companion, " + viewDom.typeDom.nested().viewCompanion() + ".companion," + Strings.quote(lensName) + ");}";
    }
    @Override public String forFromJson(String fieldName) {
        return "IResourceList.fromList(Lists.map(jsonParser.asList(j, " + Strings.quote(fieldName) + "), child ->" + companionName.asString() + ".companion.fromJson(jsonParser, child)))";
    }
    @Override public String lensDefn(String fieldName) { return fieldName + "/*" + entityNameForLens; }
    @Override public boolean isAssignableFrom(TypeDom other) {
        return other.getClass().equals(getClass()) && nested().isAssignableFrom(other.nested());
    }
    @Override public String forToJson(String fieldName, boolean templated) { return "jsonWriter.makeList(Lists.map(" + fieldName + ".toList(), a -> a.toJson(jsonWriter,context)))"; }


    @Override public TypeDom nested() { return nested; }
}
