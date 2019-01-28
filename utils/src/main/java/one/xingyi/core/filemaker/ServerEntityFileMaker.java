package one.xingyi.core.filemaker;
import one.xingyi.core.annotations.XingYiGenerated;
import one.xingyi.core.codeDom.EntityDom;
import one.xingyi.core.codeDom.FieldDom;
import one.xingyi.core.codeDom.FieldListDom;
import one.xingyi.core.marshelling.ContextForJson;
import one.xingyi.core.marshelling.HasJson;
import one.xingyi.core.marshelling.JsonParser;
import one.xingyi.core.marshelling.JsonWriter;
import one.xingyi.core.utils.Formating;
import one.xingyi.core.utils.Lists;
import one.xingyi.core.utils.Strings;
import one.xingyi.core.validation.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
public class ServerEntityFileMaker implements IFileMaker<EntityDom> {

    String callConstructor(String entityName, FieldListDom dom) {
        return "new " + entityName + "(" + dom.mapJoin(",", fd -> fd.name) + ")";
    }

    List<String> fields(FieldListDom dom) {
        return dom.map(nv -> "final " + nv.typeDom.forEntity() + " " + nv.name + ";");
    }

    List<String> constructor(EntityDom dom) {
        List<String> result = new ArrayList<>();
        result.add("@XingYiGenerated");
        result.add("public " + dom.entityNames.serverEntity.className + "(" + dom.fields.mapJoin(",", nv -> nv.typeDom.forEntity() + " " + nv.name) + "){");
        result.addAll(dom.fields.map(nv -> Formating.indent + "this." + nv.name + "=" + nv.name + ";"));
        result.add("}");
        return result;
    }

    List<String> allFieldsAccessors(String entityName, FieldListDom dom) { return dom.flatMap(fd -> accessors(entityName, callConstructor(entityName, dom), fd)); }

    List<String> accessors(String entityName, String constructor, FieldDom dom) {
        List<String> result = new ArrayList<>();
        result.add("public " + dom.typeDom.forEntity() + " " + dom.name + "() { return " + dom.name + ";}");
        if (!dom.readOnly) {
            result.add("public " + entityName + " with" + dom.name + "(" + dom.typeDom.forEntity() + " " + dom.name + "){ return " + constructor + "; } ");
        }
        return result;
    }

    List<String> makeJson(FieldListDom dom) {
        List<String> result = new ArrayList<>();
        result.add("@XingYiGenerated");
        result.add("public <J> J toJson(JsonWriter<J> jsonWriter, ContextForJson context) {");
        result.add(Formating.indent + "return jsonWriter.makeObject(" + dom.mapJoin(",", fd -> "\"" + fd.name + "\", " + fd.typeDom.forToJson(fd.name, fd.templated)) + ");");
        result.add("}");
        return result;
    }
    List<String> makeJsonForLinks(EntityDom entityDom, String companionName, String entityName, FieldListDom dom) {
        List<String> result = new ArrayList<>();
        if (entityDom.bookmark.isEmpty()) return result;
        result.add("@XingYiGenerated");
        result.add("public <J> J toJsonWithLinks(JsonWriter<J> jsonWriter, ContextForJson context, Function<" + entityName + ", String> stateFn) {");
        result.add(Formating.indent + "return jsonWriter.makeObject(" + dom.mapJoin(",",
                fd -> "\"" + fd.name + "\", " + fd.typeDom.forToJson(fd.name, fd.templated)) + ",\"links_\",context.links(jsonWriter, this, stateFn," + companionName + ".companion.bookmarkAndUrl().urlPattern, " + companionName + ".companion.stateMap));");
        result.add("}");
        return result;
    }


    public List<String> createEquals(EntityDom entityDom) {
        List<String> result = new ArrayList<>();
        result.add("@XingYiGenerated");
        result.add("@Override public boolean equals(Object o) {");
        result.add(Formating.indent + "if (this == o) return true;");
        result.add(Formating.indent + "if (o == null || getClass() != o.getClass()) return false;");
        result.add(Formating.indent + entityDom.entityNames.serverEntity.className + " other = (" + entityDom.entityNames.serverEntity.className + ") o;");
        result.add(Formating.indent + "return " + entityDom.fields.mapJoin(" && ", fd -> "Objects.equals(" + fd.name + ",other." + fd.name + ")") + ";");
        result.add("}");
        return result;
    }
    public List<String> createHashcode(EntityDom entityDom) {
        List<String> result = new ArrayList<>();
        result.add("@XingYiGenerated");
        result.add("@Override public int hashCode() {");
        result.add(Formating.indent + "return Objects.hash(" + entityDom.fields.mapJoin(",", fd -> fd.name) + ");");
        result.add("}");
        return result;
    }
    public List<String> createToString(EntityDom entityDom) {
        List<String> result = new ArrayList<>();
        result.add("@XingYiGenerated");
        result.add("@Override public String toString(){");
        result.add(Formating.indent + "return " + Strings.quote(entityDom.entityNames.serverEntity.className + "(") + "+" +
                entityDom.fields.mapJoin("+" + Strings.quote(",") + "+", fd -> fd.name) + "+" + Strings.quote(")") + ";");
        result.add("}");
        return result;

    }


    @Override public Result<String, FileDefn> apply(EntityDom entityDom) {
        String result = Lists.join(Lists.append(
                Formating.javaFile(getClass(), entityDom.entityNames.originalDefn, "class", entityDom.entityNames.serverEntity, " implements HasJson<ContextForJson>," + entityDom.entityNames.serverInterface.asString(),
                        List.of(), XingYiGenerated.class, Objects.class, JsonWriter.class, ContextForJson.class, HasJson.class, Function.class, ContextForJson.class, JsonParser.class),
                Formating.indent(fields(entityDom.fields)),
                Formating.indent(constructor(entityDom)),
                Formating.indent(allFieldsAccessors(entityDom.entityNames.serverEntity.className, entityDom.fields)),
                Formating.indent(makeJson(entityDom.fields)),
                Formating.indent(makeJsonForLinks(entityDom, entityDom.entityNames.serverCompanion.asString(), entityDom.entityNames.serverEntity.asString(), entityDom.fields)),
                Formating.indent(createEquals(entityDom)),
                Formating.indent(createHashcode(entityDom)),
                Formating.indent(createToString(entityDom)),
                List.of("/*" + entityDom + "*/"),
                List.of("}")
        ), "\n");
        return Result.succeed(new FileDefn(entityDom.entityNames.serverEntity, result));
    }
}
