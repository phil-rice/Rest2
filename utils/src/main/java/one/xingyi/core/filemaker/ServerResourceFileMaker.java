package one.xingyi.core.filemaker;
import one.xingyi.core.annotations.XingYiGenerated;
import one.xingyi.core.codeDom.ResourceDom;
import one.xingyi.core.codeDom.FieldDom;
import one.xingyi.core.codeDom.FieldListDom;
import one.xingyi.core.marshelling.*;
import one.xingyi.core.utils.Formating;
import one.xingyi.core.utils.Lists;
import one.xingyi.core.utils.Strings;
import one.xingyi.core.validation.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
public class ServerResourceFileMaker implements IFileMaker<ResourceDom> {

    String callConstructor(String entityName, FieldListDom dom) {
        return "new " + entityName + "(" + dom.noDeprecatedmapJoin(",", fd -> fd.name) + ")";
    }

    List<String> fields(FieldListDom dom) {
        return dom.noDeprecatedmap(nv -> "final " + nv.typeDom.forEntity() + " " + nv.name + ";");
    }

    List<String> constructor(ResourceDom dom) {
        List<String> result = new ArrayList<>();
        result.add("@XingYiGenerated");
        result.add("public " + dom.entityNames.serverEntity.className + "(" + dom.fields.noDeprecatedmapJoin(",", nv -> nv.typeDom.forEntity() + " " + nv.name) + "){");
        result.addAll(dom.fields.noDeprecatedmap(nv -> Formating.indent + "this." + nv.name + "=" + nv.name + ";"));
        result.add("}");
        return result;
    }

    List<String> allFieldsAccessors(String entityName, FieldListDom dom) { return dom.noDeprecatedflatMap(fd -> accessors(entityName, callConstructor(entityName, dom), fd)); }

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
        result.add(Formating.indent + "return jsonWriter.makeObject(" + dom.noDeprecatedmapJoin(",", fd -> "\"" + fd.name + "\", " + fd.typeDom.forToJson(fd.name, fd.templated)) + ");");
        result.add("}");
        return result;
    }
    List<String> makeJsonForLinks(ResourceDom resourceDom, String companionName, String entityName, FieldListDom dom) {
        List<String> result = new ArrayList<>();
        if (resourceDom.bookmark.isEmpty()) return result;
        result.add("@XingYiGenerated");
        result.add("//If you get a compilation error here is it because you have no fields defined? ");
        result.add("public <J> J toJsonWithLinks(JsonWriter<J> jsonWriter, ContextForJson context, Function<" + entityName + ", String> stateFn) {");
        result.add(Formating.indent + "return jsonWriter.makeObject(" + dom.noDeprecatedmapJoin(",",
                fd -> "\"" + fd.name + "\", " + fd.typeDom.forToJson(fd.name, fd.templated)) + ",\"links_\",context.links(jsonWriter, this, stateFn," + companionName + ".companion.stateMap));");
        result.add("}");
        return result;
    }


    public List<String> createEquals(ResourceDom resourceDom) {
        List<String> result = new ArrayList<>();
        result.add("@XingYiGenerated");
        result.add("@Override public boolean equals(Object o) {");
        result.add(Formating.indent + "if (this == o) return true;");
        result.add(Formating.indent + "if (o == null || getClass() != o.getClass()) return false;");
        result.add(Formating.indent + resourceDom.entityNames.serverEntity.className + " other = (" + resourceDom.entityNames.serverEntity.className + ") o;");
        result.add(Formating.indent + "return " + resourceDom.fields.noDeprecatedmapJoin(" && ", fd -> "Objects.equals(" + fd.name + ",other." + fd.name + ")") + ";");
        result.add("}");
        return result;
    }
    public List<String> createHashcode(ResourceDom resourceDom) {
        List<String> result = new ArrayList<>();
        result.add("@XingYiGenerated");
        result.add("@Override public int hashCode() {");
        result.add(Formating.indent + "return Objects.hash(" + resourceDom.fields.noDeprecatedmapJoin(",", fd -> fd.name) + ");");
        result.add("}");
        return result;
    }
    public List<String> createToString(ResourceDom resourceDom) {
        List<String> result = new ArrayList<>();
        result.add("@XingYiGenerated");
        result.add("@Override public String toString(){");
        result.add("//If you get a compilation error here, it might be because there are no fields defined");
        result.add(Formating.indent + "return " + Strings.quote(resourceDom.entityNames.serverEntity.className + "(") + "+" +
                resourceDom.fields.noDeprecatedmapJoin("+" + Strings.quote(",") + "+", fd -> fd.name) + "+" + Strings.quote(")") + ";");
        result.add("}");
        return result;

    }


    @Override public Result<String, FileDefn> apply(ResourceDom resourceDom) {
        String classPostFix = " implements HasJson<ContextForJson>," + resourceDom.entityNames.serverInterface.asString();
        if (resourceDom.bookmark.isPresent()) classPostFix += ",HasJsonWithLinks<ContextForJson," + resourceDom.entityNames.serverEntity.className + ">";
        String result = Lists.join(Lists.append(
                Formating.javaFile(getClass(), resourceDom.deprecated, resourceDom.entityNames.originalDefn, "class", resourceDom.entityNames.serverEntity, classPostFix,
                        List.of(), XingYiGenerated.class, Objects.class, JsonWriter.class, HasJsonWithLinks.class,
                        ContextForJson.class, HasJson.class, Function.class, ContextForJson.class, JsonParser.class, Lists.class),
                Formating.indent(fields(resourceDom.fields)),
                Formating.indent(constructor(resourceDom)),
                Formating.indent(allFieldsAccessors(resourceDom.entityNames.serverEntity.className, resourceDom.fields)),
                Formating.indent(makeJson(resourceDom.fields)),
                Formating.indent(makeJsonForLinks(resourceDom, resourceDom.entityNames.serverCompanion.asString(), resourceDom.entityNames.serverEntity.asString(), resourceDom.fields)),
                Formating.indent(createEquals(resourceDom)),
                Formating.indent(createHashcode(resourceDom)),
                Formating.indent(createToString(resourceDom)),
                List.of("/*" + resourceDom + "*/"),
                List.of("}")
        ), "\n");
        return Result.succeed(new FileDefn(resourceDom.entityNames.serverEntity, result));
    }
}
