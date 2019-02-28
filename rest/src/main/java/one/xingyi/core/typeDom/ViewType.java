package one.xingyi.core.typeDom;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.utils.Strings;
@EqualsAndHashCode
@RequiredArgsConstructor
@ToString
public class ViewType implements NonPrimitiveTypeDom {
    final String typeName;
    final String forEntity;
    final String forView;
    final String viewCompanion;
    final String serverCompanion;
    final String entityNameForLens;

    @Override public String fullTypeName() { return typeName; }
    @Override public String forEntity() { return forEntity; }
    @Override public String forView() { return forView; }
    @Override public TypeDom nested() { return this; }
    @Override public boolean primitive() { return false; }
    @Override public String entityNameForLens() { return entityNameForLens; }
    @Override public String forToJson(String fieldName, boolean templated) { return fieldName + ".toJson(jsonWriter,context)"; }
    @Override public String forFromJson(String fieldName) {
        return serverCompanion + ".companion.fromJson(jsonParser, jsonParser.child(j, " + Strings.quote(fieldName) + "))";
    }
    @Override public String lensDefn(String fieldName) { return fieldName + "/" + entityNameForLens(); }
    @Override public String viewCompanion() { return viewCompanion; }
}
