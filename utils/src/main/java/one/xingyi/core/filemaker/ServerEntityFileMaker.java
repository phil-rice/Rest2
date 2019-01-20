package one.xingyi.core.filemaker;
import one.xingyi.core.annotations.XingYiGenerated;
import one.xingyi.core.codeDom.EntityDom;
import one.xingyi.core.codeDom.FieldDom;
import one.xingyi.core.codeDom.FieldListDom;
import one.xingyi.core.marshelling.ContextForJson;
import one.xingyi.core.marshelling.HasJson;
import one.xingyi.core.marshelling.JsonTC;
import one.xingyi.core.utils.Formating;
import one.xingyi.core.utils.Lists;
import one.xingyi.core.utils.Strings;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
        result.add("public " + dom.entityName.serverEntity.className + "(" + dom.fields.mapJoin(",", nv -> nv.typeDom.forEntity() + " " + nv.name) + "){");
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
        result.add("public <J> J toJson(JsonTC<J> jsonTc, ContextForJson context) {");
        result.add(Formating.indent + "return jsonTc.makeObject(" + dom.mapJoin(",", fd -> "\"" + fd.name + "\", " + fd.typeDom.forJson(fd.name)) + ");");
        result.add("}");
        return result;
    }

    public List<String> createEquals(EntityDom entityDom) {
        List<String> result = new ArrayList<>();
        result.add("@XingYiGenerated");
        result.add("@Override public boolean equals(Object o) {");
        result.add(Formating.indent + "if (this == o) return true;");
        result.add(Formating.indent + "if (o == null || getClass() != o.getClass()) return false;");
        result.add(Formating.indent + entityDom.entityName.serverEntity.className + " other = (" + entityDom.entityName.serverEntity.className + ") o;");
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
        result.add(Formating.indent + "return " + Strings.quote(entityDom.entityName.serverEntity.className + "(") + "+" +
                entityDom.fields.mapJoin("+" + Strings.quote(",") + "+", fd -> fd.name) + "+" + Strings.quote(")") + ";");
        result.add("}");
        return result;

    }


    @Override public FileDefn apply(EntityDom entityDom) {
        String result = Lists.join(Lists.append(
                Formating.javaFile(getClass(), "class", entityDom.entityName.serverEntity, " implements HasJson<ContextForJson>," + entityDom.entityName.serverInterface.asString(),
                        List.of(), XingYiGenerated.class, Objects.class, JsonTC.class, ContextForJson.class, HasJson.class, ContextForJson.class),
                Formating.indent(fields(entityDom.fields)),
                Formating.indent(constructor(entityDom)),
                Formating.indent(allFieldsAccessors(entityDom.entityName.serverEntity.className, entityDom.fields)),
                Formating.indent(makeJson(entityDom.fields)),
                Formating.indent(createEquals(entityDom)),
                Formating.indent(createHashcode(entityDom)),
                Formating.indent(createToString(entityDom)),
                List.of("/*" + entityDom + "*/"),
                List.of("}")
        ), "\n");
        return new FileDefn(entityDom.entityName.serverEntity, result);
    }
}
