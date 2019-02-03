package one.xingyi.core.filemaker;
import one.xingyi.core.annotations.XingYiGenerated;
import one.xingyi.core.client.IResourceList;
import one.xingyi.core.codeDom.ResourceDom;
import one.xingyi.core.endpoints.BookmarkCodeAndUrlPattern;
import one.xingyi.core.endpoints.HasBookmarkAndUrl;
import one.xingyi.core.marshelling.JsonParser;
import one.xingyi.core.sdk.IXingYiServerCompanion;
import one.xingyi.core.sdk.IXingYiServesResourceCompanion;
import one.xingyi.core.state.EntityDomToStateMap;
import one.xingyi.core.state.StateData;
import one.xingyi.core.utils.*;
import one.xingyi.core.validation.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
public class ServerCompanionFileMaker implements IFileMaker<ResourceDom> {

    List<String> createBookmarkAndUrl(ResourceDom resourceDom) {
        return Optionals.fold(resourceDom.bookmark, () -> List.of(), b ->
                List.of("@Override public BookmarkCodeAndUrlPattern bookmarkAndUrl(){return new BookmarkCodeAndUrlPattern(" +
                        Strings.quote(b.bookmark) + "," + Strings.quote(b.urlPattern) + "," + Strings.quote(b.code) + ");}"));
    }


    private List<String> createCompanion(ResourceDom resourceDom) {
        return List.of("public static " + resourceDom.entityNames.serverCompanion.asString() + " companion  =new " + resourceDom.entityNames.serverCompanion.className + "();");
    }
    private List<String> createEntityEndpoint(ResourceDom resourceDom) {
        return List.of("public static " + resourceDom.entityNames.serverCompanion.asString() + " companion  =new " + resourceDom.entityNames.serverCompanion.className + "();");
    }
    private List<String> createJavascript(ResourceDom resourceDom) {
        List<String> result = new ArrayList<>();
        result.add("public String javascript(){return javascript;}");
        result.add("final public String javascript = " + Strings.quote(""));
        result.addAll(Formating.indent(resourceDom.fields.withDeprecatedmap(fd -> "+ " + Strings.quote(fd.javascript + "\\n"))));
        result.add(";");
        return result;
    }

    private List<String> createListOfLens(ResourceDom resourceDom) {
        return List.of("public List<String> lens(){return List.of(", resourceDom.fields.withDeprecatedmapJoin(",", fd -> Strings.quote(fd.lensName)), ");}");
    }

    List<String> fromJson(ResourceDom dom) {
        String className = dom.entityNames.serverEntity.className;
        return List.of("@XingYiGenerated", "public <J> " + className + " fromJson(JsonParser<J> jsonParser, J j){",
                Formating.indent + "return new " + className + "(" + dom.fields.noDeprecatedmapJoin(",\n" + Formating.indent + Formating.indent, fd -> fd.typeDom.forFromJson(fd.name)) + ");",
                "};");
    }

    @Override public Result<String, FileDefn> apply(ResourceDom resourceDom) {
        String implementsString = (resourceDom.bookmark.isEmpty() ? "IXingYiServerCompanion" : "IXingYiServesResourceCompanion") + "<" + resourceDom.entityNames.originalDefn.asString() + "," + resourceDom.entityNames.serverEntity.asString() + ">";
        String result = Lists.join(Lists.append(
                Formating.javaFile(getClass(), resourceDom.deprecated, resourceDom.entityNames.originalDefn, "class", resourceDom.entityNames.serverCompanion,
                        " implements " + implementsString,
                        List.of(resourceDom.entityNames.serverEntity.asString()),
                        IXingYiServerCompanion.class, JsonParser.class, Map.class, StateData.class, List.class, IResourceList.class, Lists.class,
                        IXingYiServesResourceCompanion.class, XingYiGenerated.class, Optional.class, BookmarkCodeAndUrlPattern.class, HasBookmarkAndUrl.class),
//                Formating.indent(allFieldsAccessors(entityDom.entityNames.serverInterface.className, entityDom.fields)),
                Formating.indent(createBookmarkAndUrl(resourceDom)),
                Formating.indent(createCompanion(resourceDom)),
                Formating.indent(createJavascript(resourceDom)),
                Formating.indent(createListOfLens(resourceDom)),
                Formating.indent(fromJson(resourceDom)),
                Formating.indent(stateMap(resourceDom)),
                List.of("}")
        ), "\n");
        return Result.succeed(new FileDefn(resourceDom.entityNames.serverCompanion, result));
    }
    private List<String> stateMap(ResourceDom resourceDom) {
        Map<String, List<StateData>> statemap = EntityDomToStateMap.simple.apply(resourceDom);
        return List.of("public final Map<String,List<StateData>> stateMap=Map.of(",
                Formating.indent + Lists.mapJoin(Sets.sortedList(statemap.keySet(), String::compareTo), ",\n" + Formating.indent + Formating.indent,
                        stateName -> Strings.quote(stateName) + ", List.of(" + Lists.mapJoin(statemap.get(stateName), ",", (sd -> "new StateData(" + Strings.quote(sd.action) + "," + Strings.quote(sd.link) + "))"))),
                ");");
    }
}
