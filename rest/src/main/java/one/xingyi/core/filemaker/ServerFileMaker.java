package one.xingyi.core.filemaker;
import one.xingyi.core.annotations.XingYiGenerated;
import one.xingyi.core.codeDom.ServerDom;
import one.xingyi.core.endpoints.*;
import one.xingyi.core.marshelling.JsonValue;
import one.xingyi.core.marshelling.JsonWriter;
import one.xingyi.core.sdk.IXingYiServer;
import one.xingyi.core.sdk.IXingYiServerCompanion;
import one.xingyi.core.server.EndpointHandler;
import one.xingyi.core.server.SimpleServer;
import one.xingyi.core.utils.*;
import one.xingyi.core.validation.Result;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class ServerFileMaker implements IFileMaker<ServerDom> {

    List<String> makeCompanions(ServerDom serverDom) {
        return List.of(
                "public List<IXingYiServerCompanion<?, ?>> companions() {return List.of(",
                Formating.indent + Lists.mapJoin(serverDom.codeDom.resourceDoms, ",", ed -> ed.entityNames.serverCompanion.asString()) + ");}");
    }

    List<String> createFields(ServerDom serverDom) {
        return Lists.<String>append(
                List.of("public final EndpointContext<J> context;"),
                Lists.collect(serverDom.codeDom.resourceDoms, ed -> ed.bookmark.isPresent(), ed -> "public final " + ed.entityNames.serverController.asString() + " " + ed.entityNames.serverController.className + ";")
        );
    }
    List<String> createConstructor(ServerDom serverDom) {
        return Lists.<String>append(
                List.of("public " + serverDom.serverName.className + "(EndpointConfig<J> config," +
                                Lists.collectJoin(serverDom.codeDom.resourceDoms, ",", ed -> ed.bookmark.isPresent(), ed -> ed.entityNames.serverController.asVariableDeclaration()) + "){// A compilation error here is often caused by incremental compilation. It might also be because there are no bookmarked/rootUrl resouces (see the @Resource annotation)",
                        Formating.indent + "this.context = config.from(companions());"),
                Formating.indent(Lists.collect(serverDom.codeDom.resourceDoms, ed -> ed.bookmark.isPresent(), ed -> "this." + ed.entityNames.serverController.className + " = " + ed.entityNames.serverController.className + ";")),
                List.of("}")
        );
    }

    List<String> createCompanions(ServerDom serverDom) {
        return List.of(
                "public List<IXingYiServerCompanion<?, ?>> companions(){return List.of(",
                Formating.indent + "one.xingyi.core.httpClient.server.companion.ResourceDetailsCompanion.companion,//added for url pattern on original bookmark ",
                Formating.indent + Lists.mapJoin(serverDom.codeDom.resourceDoms, ",", ed -> ed.entityNames.serverCompanion.asString() + ".companion"),
                ");}");
    }
    List<String> createLens(ServerDom serverDom) {
        return List.of("public List<String> lens(){return Lists.flatMap(companions(), c->c.lens());}");
    }


    List<String> createEntityCompanions(ServerDom serverDom) {
        return List.of(
                "public List<HasBookmarkAndUrl> entityCompanions(){return List.of(",
                Formating.indent + Lists.mapJoin(Lists.filter(serverDom.codeDom.resourceDoms, e -> e.bookmark.isPresent()), ",", ed -> ed.entityNames.serverCompanion.asString() + ".companion"),
                ");}");
    }

    String bookmarkAndCode(BookmarkCodeAndUrlPattern b) {return Strings.quote(b.urlPattern) + "," + Strings.quote(b.code);}

    List<String> createWithNoIdEndpoint(String entityName, String path, String companion, String fromFn, String function, String stateFn) {
        return Lists.append(List.of(
                "public EndPoint createWithNoId" + entityName + "() {",
                Formating.indent + "return " + companion + ".companion.endpoints(context, " + stateFn + ").createWithoutId(" + Strings.quote(path) + "," + fromFn + "," + function + ");",
                "}"));
    }
    List<String> createEndpointWithStateFn(String entityName, String companion, String function, String stateFn) {
        return Lists.append(List.of(
                "public EndPoint createWithId" + entityName + "() {",
                Formating.indent + " return " + companion + ".companion.endpoints(context, " + stateFn + ").createWithId(" + function + ");",
                "}"));
    }
    List<String> getEndpointWithStateFn(String entityName, String method, String companion, String function, String stateFn) {
        return Lists.append(List.of(
                "public EndPoint get" + entityName + "() {",
                Formating.indent + " return " + companion + ".companion.endpoints(context, " + stateFn + ")." + method + "(" + function + ");",
                "}"));
    }
    List<String> createEndpoint(String methodName, String method, BookmarkCodeAndUrlPattern bookmark, String function) {
        return Lists.append(List.of(
                "public EndPoint " + methodName + "() {",
                Formating.indent + "return EndPoint." + method + "(context, " + bookmarkAndCode(bookmark) + ", " + function + ");",
                "}"));
    }
    List<String> deleteEndpoint(String entityName, String companion, String function, String stateFn) {
        return List.of(
                "public EndPoint delete" + entityName + "() {",
                Formating.indent + "return " + companion + ".companion.endpoints(context, " + stateFn + ").delete(" + function + ");",
                "}");
    }
    //className, companionName, controllerName + "::put", controllerName + "::stateFn")
    List<String> createPutEndpoint(String className, String companionName, String function, String stateFn) {
        return List.of(
                "public EndPoint put" + className + "() {",
                Formating.indent + "return " + companionName + ".companion.endpoints(context, " + stateFn + ").put(",
                Formating.indent + Formating.indent + "(sr, id) -> new IdAndValue<>(id, " + companionName + ".companion.fromJson(context.parserAndWriter, context.parserAndWriter.parse(sr.body))), " + function + ");",
                "}");

    }


    List<String> createPostEndpoint(String postName, String url, String companionName, String function, String stateFn, List<String> states) {
        return Lists.append(List.of(
                "public EndPoint " + postName + "() {",
                Formating.indent + "return " + companionName + ".companion.endpoints(context, " + stateFn + ").",
                Formating.indent + "post(" + Strings.quote(url) + ", List.of(" + Lists.mapJoin(states, ",", Strings::quote) + ")," + function + ");",
                "}"));
    }
    List<String> createEntityEndpoint(ServerDom serverDom) {
        return List.of(
                "//A compilation error here might be because you haven't added a maven dependency to the 'core' jar",
                "public EndPoint entityEndpoint(){ return new ResourceDefinitionEndPoint(context,entityCompanions());}");
    }
    List<String> createCodeEndpoint(ServerDom serverDom) {
        return Lists.map(serverDom.codeDom.servedresourceDoms, rd -> "public EndPoint " + rd.entityNames.serverEntity.className +
                "codeEndpoint() {return EndPoint.javascript(context, " + Strings.quote(rd.bookmark.get().code) + ");}");
    }

    List<String> createEndpoints(ServerDom serverDom) {
        return Lists.flatMap(serverDom.codeDom.resourceDoms, ed -> {
            String className = ed.entityNames.serverEntity.className;
            String controllerName = ed.entityNames.serverController.className;
            String companionName = ed.entityNames.serverCompanion.className;
            return Optionals.fold(ed.bookmark, () -> List.of(), b -> Lists.<String>append(
                    List.of("//EntityDom: " + ed.bookmark),
                    Optionals.flatMap(ed.actionsDom.createDom, dom -> createEndpointWithStateFn(className, companionName, controllerName + "::createWithId", controllerName + "::stateFn")),
                    Optionals.flatMap(ed.actionsDom.createWithoutIdDom, dom -> createWithNoIdEndpoint(className, dom.path, companionName, controllerName + "::createWithoutIdRequestFrom", controllerName + "::createWithoutId", controllerName + "::stateFn")),
                    Optionals.flatMap(ed.actionsDom.putDom, dom -> createPutEndpoint(className, companionName, controllerName + "::put", controllerName + "::stateFn")),
                    Optionals.flatMap(ed.actionsDom.getDom, dom -> dom.mustExist ?
                            getEndpointWithStateFn(className, "get", companionName, controllerName + "::get", controllerName + "::stateFn") :
                            getEndpointWithStateFn(className, "getOptional", companionName, controllerName + "::getOptional", controllerName + "::stateFn")),
                    Optionals.flatMap(ed.actionsDom.deleteDom, dom -> deleteEndpoint(className, companionName, controllerName + "::delete", controllerName + "::stateFn")),
                    Lists.flatMap(ed.actionsDom.postDoms, dom -> createPostEndpoint(dom.action + className,
                            b.withMoreUrl(dom.path).urlPattern, companionName, controllerName + "::" + dom.action, controllerName + "::stateFn", dom.states))));
        });
    }
    @Override public Result<String, FileDefn> apply(ServerDom serverDom) {
        List<String> manualImports = Lists.append(
                Lists.map(serverDom.codeDom.resourceDoms, rd -> rd.entityNames.serverCompanion.asString()),
                List.of("one.xingyi.core.ResourceDefinitionEndPoint  /* A compile error here might be because you haven't included the maven dependency for 'org.xingyi.core' */"));
        String result = Lists.join(Lists.append(
                Formating.javaFile(getClass(), false, serverDom.originalDefn, "class", serverDom.serverName, "<J> implements IXingYiServer",
                        manualImports,
                        XingYiGenerated.class, EndPoint.class, List.class, Lists.class, EndpointConfig.class, EndpointContext.class, IXingYiServer.class,
                        ExecutorService.class, SimpleServer.class, Executors.class, EndpointHandler.class, IXingYiServerCompanion.class,
                        JsonValue.class, JsonWriter.class, Files.class, HasBookmarkAndUrl.class, IdAndValue.class),
//                Formating.indent(generateRegister(serverDom)),
                Formating.indent(createFields(serverDom)),
                Formating.indent(createConstructor(serverDom)),
                Formating.indent(createEntityCompanions(serverDom)),
                Formating.indent(createCompanions(serverDom)),
                Formating.indent(createEntityEndpoint(serverDom)),
                Formating.indent(createCodeEndpoint(serverDom)),
                Formating.indent(createEndpoints(serverDom)),
                Formating.indent(createLens(serverDom)),
//                Formating.indent(makeSimpleServer(serverDom)),
                List.of("/*" + serverDom),
//                List.of("//gets"),
//                Formating.indent(Lists.map(serverDom.defnNames, Objects::toString)),
                List.of("//entities"),
                Formating.indent(Lists.map(serverDom.codeDom.resourceDoms, Objects::toString)),
                List.of("*/"),
                List.of("}")
        ), "\n");
        return Result.succeed(new FileDefn(serverDom.serverName, result));
    }
}
