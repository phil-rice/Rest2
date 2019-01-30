package one.xingyi.core.filemaker;
import one.xingyi.core.annotations.XingYiGenerated;
import one.xingyi.core.client.ISimpleList;
import one.xingyi.core.codeDom.EntityDom;
import one.xingyi.core.endpoints.BookmarkAndUrlPattern;
import one.xingyi.core.endpoints.HasBookmarkAndUrl;
import one.xingyi.core.marshelling.JsonParser;
import one.xingyi.core.sdk.IXingYiServerCompanion;
import one.xingyi.core.sdk.IXingYiServesEntityCompanion;
import one.xingyi.core.state.EntityDomToStateMap;
import one.xingyi.core.state.StateData;
import one.xingyi.core.utils.*;
import one.xingyi.core.validation.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
public class ServerCompanionFileMaker implements IFileMaker<EntityDom> {

    List<String> createBookmarkAndUrl(EntityDom entityDom) {
        return Optionals.fold(entityDom.bookmark, () -> List.of(), b -> List.of("@Override public BookmarkAndUrlPattern bookmarkAndUrl(){return new BookmarkAndUrlPattern(" + Strings.quote(b.bookmark) + "," + Strings.quote(b.urlPattern) + ");}"));
    }


    private List<String> createCompanion(EntityDom entityDom) {
        return List.of("public static " + entityDom.entityNames.serverCompanion.asString() + " companion  =new " + entityDom.entityNames.serverCompanion.className + "();");
    }
    private List<String> createEntityEndpoint(EntityDom entityDom) {
        return List.of("public static " + entityDom.entityNames.serverCompanion.asString() + " companion  =new " + entityDom.entityNames.serverCompanion.className + "();");
    }
    private List<String> createJavascript(EntityDom entityDom) {
        List<String> result = new ArrayList<>();
        result.add("public String javascript(){return javascript;}");
        result.add("final public String javascript = " + Strings.quote(""));
        result.addAll(Formating.indent(entityDom.fields.withDeprecatedmap(fd -> "+ " + Strings.quote(fd.javascript + "\\n"))));
        result.add(";");
        return result;
    }

    private List<String> createListOfLens(EntityDom entityDom) {
        return List.of("public List<String> lens(){return List.of(", entityDom.fields.withDeprecatedmapJoin(",", fd -> Strings.quote(fd.lensName)), ");}");
    }

    List<String> fromJson(EntityDom dom) {
        String className = dom.entityNames.serverEntity.className;
        return List.of("@XingYiGenerated", "public <J> " + className + " fromJson(JsonParser<J> jsonParser, J j){",
                Formating.indent + "return new " + className + "(" + dom.fields.noDeprecatedmapJoin(",\n"+Formating.indent+Formating.indent, fd -> fd.typeDom.forFromJson(fd.name)) + ");",
                "};");
    }

    @Override public Result<String, FileDefn> apply(EntityDom entityDom) {
        String implementsString = (entityDom.bookmark.isEmpty() ? "IXingYiServerCompanion" : "IXingYiServesEntityCompanion") + "<" + entityDom.entityNames.originalDefn.asString() + "," + entityDom.entityNames.serverEntity.asString() + ">";
        String result = Lists.join(Lists.append(
                Formating.javaFile(getClass(), entityDom.entityNames.originalDefn, "class", entityDom.entityNames.serverCompanion,
                        " implements " + implementsString,
                        List.of(entityDom.entityNames.serverEntity.asString()),
                        IXingYiServerCompanion.class, JsonParser.class, Map.class, StateData.class, List.class, ISimpleList.class,Lists.class,
                        IXingYiServesEntityCompanion.class, XingYiGenerated.class, Optional.class, BookmarkAndUrlPattern.class, HasBookmarkAndUrl.class),
//                Formating.indent(allFieldsAccessors(entityDom.entityNames.serverInterface.className, entityDom.fields)),
                Formating.indent(createBookmarkAndUrl(entityDom)),
                Formating.indent(createCompanion(entityDom)),
                Formating.indent(createJavascript(entityDom)),
                Formating.indent(createListOfLens(entityDom)),
                Formating.indent(fromJson(entityDom)),
                Formating.indent(stateMap(entityDom)),
                List.of("}")
        ), "\n");
        return Result.succeed(new FileDefn(entityDom.entityNames.serverCompanion, result));
    }
    private List<String> stateMap(EntityDom entityDom) {
        Map<String, List<StateData>> statemap = EntityDomToStateMap.simple.apply(entityDom);
        return List.of("public final Map<String,List<StateData>> stateMap=Map.of(",
                Formating.indent + Lists.mapJoin(Sets.sortedList(statemap.keySet(), String::compareTo), ",\n" + Formating.indent + Formating.indent,
                        stateName -> Strings.quote(stateName) + ", List.of(" + Lists.mapJoin(statemap.get(stateName), ",", (sd -> "new StateData(" + Strings.quote(sd.action) + "," + Strings.quote(sd.link) + "))"))),
                ");");
    }
}
