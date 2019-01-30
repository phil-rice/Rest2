package one.xingyi.core.typeDom;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.client.ISimpleList;
import one.xingyi.core.codeDom.PackageAndClassName;
import one.xingyi.core.utils.Strings;
@EqualsAndHashCode
@RequiredArgsConstructor
@ToString

public class ListType implements TypeDom {
    final String fullTypeName;
    final TypeDom nested;
    final PackageAndClassName companionName;
    @Override public String forEntity() { return ISimpleList.class.getName() + "<" + nested.forEntity() + ">"; }
    @Override public String forView() { return ISimpleList.class.getName() + "<" + nested.forView() + ">"; }
    @Override public String fullTypeName() { return fullTypeName; }
    @Override public boolean primitive() { return false; }
    @Override public String forFromJson(String fieldName) {
        return "ISimpleList.fromList(Lists.map(jsonParser.asList(j, " + Strings.quote(fieldName) + "), child ->" + companionName.asString() + ".companion.fromJson(jsonParser, child)))";
    }
    @Override public String forToJson(String fieldName, boolean templated) { return "jsonWriter.makeList(Lists.map(" + fieldName + ".toList(), a -> a.toJson(jsonWriter,context)))"; }


    @Override public TypeDom nested() { return nested; }

}
