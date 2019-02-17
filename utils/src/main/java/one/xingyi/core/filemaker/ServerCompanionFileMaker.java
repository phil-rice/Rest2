package one.xingyi.core.filemaker;
import one.xingyi.core.annotations.Resource;
import one.xingyi.core.annotations.XingYiGenerated;
import one.xingyi.core.client.IResourceList;
import one.xingyi.core.codeDom.FieldDom;
import one.xingyi.core.codeDom.ResourceDom;
import one.xingyi.core.codeDom.ServerDom;
import one.xingyi.core.endpoints.BookmarkCodeAndUrlPattern;
import one.xingyi.core.endpoints.EndPoint;
import one.xingyi.core.endpoints.EndpointContext;
import one.xingyi.core.endpoints.HasBookmarkAndUrl;
import one.xingyi.core.marshelling.JsonParser;
import one.xingyi.core.mediatype.IResourceEndpoints;
import one.xingyi.core.mediatype.IXingYiServerMediaTypeDefn;
import one.xingyi.core.mediatype.JsonAndLensDefnServerMediaTypeDefn;
import one.xingyi.core.mediatype.ServerMediaTypeContext;
import one.xingyi.core.optics.lensLanguage.*;
import one.xingyi.core.sdk.IXingYiServerCompanion;
import one.xingyi.core.sdk.IXingYiServesResourceCompanion;
import one.xingyi.core.state.EntityDomToStateMap;
import one.xingyi.core.state.StateData;
import one.xingyi.core.typeDom.TypeDom;
import one.xingyi.core.utils.*;
import one.xingyi.core.validation.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
public class ServerCompanionFileMaker implements IFileMaker<ResourceDom> {

    List<String> createBookmarkAndUrl(ResourceDom resourceDom) {
        return Optionals.fold(resourceDom.bookmark, () -> List.of(), b ->
                List.of("@Override public BookmarkCodeAndUrlPattern bookmarkAndUrl(){return new BookmarkCodeAndUrlPattern(" +
                        Strings.quote(b.bookmark) + "," + Strings.quote(b.urlPattern) + "," + Strings.quote(b.code) + ");}"));
    }


    private List<String> createCompanion(ResourceDom resourceDom) {
        return List.of("public static " + resourceDom.entityNames.serverCompanion.asString() + " companion  =new " + resourceDom.entityNames.serverCompanion.className + "();");
    }
    //    private List<String> createEntityEndpoint(ResourceDom resourceDom) {
//        return List.of("public static " + resourceDom.entityNames.serverCompanion.asString() + " companion  =new " + resourceDom.entityNames.serverCompanion.className + "();");
//    }
    private List<String> createJavascript(ResourceDom resourceDom) {
        List<String> result = new ArrayList<>();
        result.add("public String javascript(){return javascript;}");
        result.add("final public String javascript = " + Strings.quote(""));
        result.addAll(Formating.indent(resourceDom.fields.withDeprecatedmap(fd -> "+ " + Strings.quote(fd.javascript + "\\n"))));
        result.add(";");
        return result;
    }

    private List<String> createMediaDefn(ResourceDom resourceDom) {
        if (resourceDom.bookmark.isPresent()) {
            String entityName = resourceDom.entityNames.serverEntity.className;
            return List.of(" public <J>IXingYiServerMediaTypeDefn<" + resourceDom.entityNames.serverEntity.className + "> lensMediaDefn(ServerMediaTypeContext<J> context) {",
                    Formating.indent + "return new JsonAndLensDefnServerMediaTypeDefn<>(" + Strings.quote(entityName) + ", this, context, lensLines());",
                    " }");
        } else return List.of();
    }

    private List<String> createListOfLens(ResourceDom resourceDom) {
        return List.of("public List<String> lens(){return List.of(", resourceDom.fields.withDeprecatedmapJoin(",", fd -> Strings.quote(fd.lensName)), ");}");
    }

    //TODO Sort this out. This is a bodge
    List<String> lensPathCode(TypeDom typeDom, List<String> path) {
        if (path.size() == 0) throw new IllegalStateException("Path should not be of no length");
        List<String> result = Lists.map(path, p -> "new ViewLensDefn(" + Strings.quote(p) + ",\"notYes\")");
        result.set(result.size() - 1, typeDom.lensDefn(path.get(path.size() - 1)));
        return result;
    }
    private List<String> createLensDefn(ResourceDom resourceDom) {

        return List.of(
                "public List<LensLine> lensLines(){return List.of(",
                Formating.indent + resourceDom.fields.withDeprecatedmapJoin(",\n" + Formating.indent + Formating.indent, fd -> "new LensLine(" + Strings.quote(fd.lensName) + ", List.of( " +
                        Lists.join(lensPathCode(fd.typeDom, fd.lensPath), ",") + "))"),
                ");}");
    }

    List<String> fromJson(ResourceDom dom) {
        String className = dom.entityNames.serverEntity.className;
        return List.of("@XingYiGenerated", "public <J> " + className + " fromJson(JsonParser<J> jsonParser, J j){",
                Formating.indent + "return new " + className + "(" + dom.fields.noDeprecatedmapJoin(",\n" + Formating.indent + Formating.indent, fd -> fd.typeDom.forFromJson(fd.name)) + ");",
                "};");
    }


    String bookmarkAndCode(BookmarkCodeAndUrlPattern b) {return Strings.quote(b.urlPattern) + "," + Strings.quote(b.code);}
    List<String> createWithNoIdEndpoint(String methodName, String method, String
            controllerName, BookmarkCodeAndUrlPattern bookmark, String reqFn, String function, String stateFn) {
        return Lists.append(List.of(
                "public <J>EndPoint " + methodName + "(EndpointContext<J> context, " + controllerName + " controller) {",
                Formating.indent + "return EndPoint." + method + "(context, " + bookmarkAndCode(bookmark) + ", " + reqFn + "," + function + "," + stateFn + ");",
                "}"));
    }
    List<String> createEndpointWithStateFn(String methodName, String method, String
            controllerName, BookmarkCodeAndUrlPattern bookmark, String function, String stateFn) {
        return Lists.append(List.of(
                "public <J>EndPoint " + methodName + "(EndpointContext<J> context, " + controllerName + " controller) {",
                Formating.indent + "return EndPoint." + method + "(context, " + bookmarkAndCode(bookmark) + ", " + function + "," + stateFn + ");",
                "}"));
    }
    List<String> deleteEndpoint(String methodName, String method, String controllerName, BookmarkCodeAndUrlPattern
            bookmark, String function) {
        return Lists.append(List.of(
                "public <J>EndPoint " + methodName + "(EndpointContext<J> context, " + controllerName + " controller) {",
                Formating.indent + "return EndPoint." + method + "(context, " + Strings.quote(bookmark.urlPattern) + ", " + function + ");",
                "}"));
    }
    List<String> createPutEndpoint(String methodName, String companionName, String
            controllerName, BookmarkCodeAndUrlPattern bookmark, String function, String stateFn) {
        return Lists.append(List.of(
                "public <J>EndPoint " + methodName + "(EndpointContext<J> context, " + controllerName + " controller) {",
                Formating.indent + "return EndPoint.putEntity(" + companionName + ".companion, context, " + bookmarkAndCode(bookmark) + ", " + function + "," + stateFn + ");",
                "}"));
    }
    List<String> createPostEndpoint(String methodName, List<String> states, String
            controllerName, BookmarkCodeAndUrlPattern bookmark, String function, String stateFn) {
        return Lists.append(List.of(
                "public <J> EndPoint " + methodName + "(EndpointContext<J> context, " + controllerName + " controller) {",
                Formating.indent + "return EndPoint.postEntity(context, " + bookmarkAndCode(bookmark) + ", " + "List.of(" + Lists.mapJoin(states, ",", Strings::quote) + ")," + function + "," + stateFn + ");",
                "}"));
    }
    List<String> createEntityEndpoint(ResourceDom resourceDom) {
        return List.of(
                "public <J>EndPoint entityEndpoint(EndpointContext<J> context,List<HasBookmarkAndUrl> companions){ return EndPointFactorys.<J>entityEndpointFromContext(context,companions);}",
                "public  <J>EndPoint entityCodeEndpoint(EndpointContext<J> context){ return  EndPoint.javascript(context, \"{host}/resource/code\");}");
    }
    List<String> createCodeEndpoint(ResourceDom resourceDom) {
        if (resourceDom.bookmark.isPresent())
            return List.of("public <J> EndPoint " + resourceDom.entityNames.serverEntity.className + "codeEndpoint(EndpointContext<J> context) {return EndPoint.javascript(context, " + Strings.quote(resourceDom.bookmark.get().code) + ");}");
        else return List.of();
    }

    List<String> createEndpoints(ResourceDom resourceDom) {
        String className = resourceDom.entityNames.serverEntity.className;
        if (resourceDom.bookmark.isPresent()) {
            return List.of("public <J> IResourceEndpoints<" + className + "> endpoints(ServerMediaTypeContext<J> context, Function<" + className + ", String> stateFn) {",
                    Formating.indent + "return lensMediaDefn(context).endpoints(context.protocol(), bookmarkAndUrl(), stateFn);",
                    "}");
        } else return List.of();

//        String controllerName = resourceDom.entityNames.serverController.className;
//        String companionName = resourceDom.entityNames.serverCompanion.asString();
//        return Optionals.fold(resourceDom.bookmark, () -> List.of(), b -> Lists.<String>append(
//                List.of("//EntityDom: " + resourceDom.bookmark),
//                Optionals.flatMap(resourceDom.actionsDom.createDom, dom -> createEndpointWithStateFn("createWithId" + className, "createEntityWithId", controllerName, b, "controller::createWithId", "controller::stateFn")),
//                Optionals.flatMap(resourceDom.actionsDom.createWithoutIdDom, dom -> createWithNoIdEndpoint("create" + className, "createEntity", controllerName, b.withUrl(dom.path), "controller::createWithoutIdRequestFrom", "controller::createWithoutId", "controller::stateFn")),
//                Optionals.flatMap(resourceDom.actionsDom.putDom, dom -> createPutEndpoint("put" + className, companionName, controllerName, b, "controller::put", "controller::stateFn")),
//                Optionals.flatMap(resourceDom.actionsDom.getDom, dom -> dom.mustExist ?
//                        createEndpointWithStateFn("get" + className, "getEntity", controllerName, b,
//                                "controller::get", "controller::stateFn") :
//                        createEndpointWithStateFn("getOptional" + className, "getOptionalEntity", controllerName, b,
//                                "controller::getOptional", "controller::stateFn")),
//                Optionals.flatMap(resourceDom.actionsDom.deleteDom, dom -> deleteEndpoint("delete" + className, "deleteEntity", controllerName, b, "controller::delete")),
//                Lists.flatMap(resourceDom.actionsDom.postDoms, dom -> createPostEndpoint(dom.action + className, dom.states, controllerName, b.withMoreUrl(dom.path), "controller::" + dom.action, "controller::stateFn"))));
    }

    @Override public Result<String, FileDefn> apply(ResourceDom resourceDom) {
        String implementsString = (resourceDom.bookmark.isEmpty() ? "IXingYiServerCompanion" : "IXingYiServesResourceCompanion") + "<" + resourceDom.entityNames.originalDefn.asString() + "," + resourceDom.entityNames.serverEntity.asString() + ">";
        String result = Lists.join(Lists.append(
                Formating.javaFile(getClass(), resourceDom.deprecated, resourceDom.entityNames.originalDefn, "class", resourceDom.entityNames.serverCompanion,
                        " implements " + implementsString,
                        List.of(resourceDom.entityNames.serverEntity.asString(),
                                resourceDom.entityNames.serverController.asString(),
                                "one.xingyi.core.EndPointFactorys",
                                "one.xingyi.core.httpClient.server.controller.IResourceDetailsController"),
                        IXingYiServerCompanion.class, JsonParser.class, Map.class, StateData.class, List.class, IResourceList.class, Lists.class,
                        IXingYiServesResourceCompanion.class, XingYiGenerated.class, EndPoint.class, EndpointContext.class,
                        Optional.class, BookmarkCodeAndUrlPattern.class, HasBookmarkAndUrl.class, ServerMediaTypeContext.class, JsonAndLensDefnServerMediaTypeDefn.class,
                        LensLine.class, StringLensDefn.class, ViewLensDefn.class, ListLensDefn.class, IXingYiServerMediaTypeDefn.class, IResourceEndpoints.class, Function.class
                ),
//                Formating.indent(allFieldsAccessors(entityDom.entityNames.serverInterface.className, entityDom.fields)),
                Formating.indent(createBookmarkAndUrl(resourceDom)),
                Formating.indent(createEndpoints(resourceDom)),
                Formating.indent(createCodeEndpoint(resourceDom)),
                Formating.indent(createEntityEndpoint(resourceDom)),
                Formating.indent(createCompanion(resourceDom)),
                Formating.indent(createJavascript(resourceDom)),
                Formating.indent(createListOfLens(resourceDom)),
                Formating.indent(createMediaDefn(resourceDom)),
                Formating.indent(createLensDefn(resourceDom)),
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
