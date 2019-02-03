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
    //    List<String> makeSimpleServer(ServerDom serverDom) {
//        return List.of("public static SimpleServer simple(){",
//                Formating.indent + "ExecutorService executorService = Executors.newFixedThreadPool(20);",
//                Formating.indent + "EndpointConfig<JsonObject> config = new EndpointConfig<>(Files.getText(\"header.js\"), JsonWriter.cheapJson, \"http://\");",
//                Formating.indent + "return  new SimpleServer(executorService, new EndpointHandler(entityEndpoints(config)), " + serverDom.port + ");",
//                "}");
//    }

//    List<String> createEndPoints(ServerDom serverDom) {
//        return Lists.append(
//                List.of("public static <J> EndPoint entityEndpoints(EndpointConfig<J> config) { return EndPointFactorys.apply(config, List.of(",
//                        Lists.join(Formating.indent(Lists.map(serverDom.defnNames,
//                                n -> "new GetEntityEndpointDetails<>(" + n.entityNames.serverCompanion.asString() + ".companion, new " + n.getName + "())")),
//                                ",\n" + Formating.indent) + "),"),
//                List.of("List.of(",
//                        Lists.join(Formating.indent(Lists.map(serverDom.codeDom.entityDoms, ed -> ed.entityNames.serverCompanion.asString() + ".companion")), ",\n" + Formating.indent),
//                        "));}"));
//        //    static EndPoint entityEndpoints = EndPointFactorys.apply(config,
//        //            List.of(
//        //                    new GetEntityEndpointDetails<>(PersonCompanion.companion, new PersonGet()),
//        //                    new GetEntityEndpointDetails<>(AddressCompanion.companion, new AddressGet())),
//        //            List.of(TelephoneNumberCompanion.companion));
//    }

    List<String> makeCompanions(ServerDom serverDom) {
        return List.of(
                "public List<IXingYiServerCompanion<?, ?>> companions() {return List.of(",
                Formating.indent + Lists.mapJoin(serverDom.codeDom.resourceDoms, ",", ed -> ed.entityNames.serverCompanion.asString()) + ");}");
    }

    List<String> createFields(ServerDom serverDom) {
        return Lists.<String>append(
                List.of("public final EndpointContext<J> context;"),
                Lists.collect(serverDom.codeDom.resourceDoms, ed -> ed.bookmark.isPresent(), ed -> ed.entityNames.serverController.asString() + " " + ed.entityNames.serverController.className + ";")
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
    List<String> createWithNoIdEndpoint(String methodName, String method, BookmarkCodeAndUrlPattern bookmark, String reqFn, String function, String stateFn) {
        return Lists.append(List.of(
                "public EndPoint " + methodName + "() {",
                Formating.indent + "return EndPoint." + method + "(context, " + bookmarkAndCode(bookmark) + ", " + reqFn + "," + function + "," + stateFn + ");",
                "}"));
    }
    List<String> createEndpointWithStateFn(String methodName, String method, BookmarkCodeAndUrlPattern bookmark, String function, String stateFn) {
        return Lists.append(List.of(
                "public EndPoint " + methodName + "() {",
                Formating.indent + "return EndPoint." + method + "(context, " + bookmarkAndCode(bookmark) + ", " + function + "," + stateFn + ");",
                "}"));
    }
    List<String> createEndpoint(String methodName, String method, BookmarkCodeAndUrlPattern bookmark, String function) {
        return Lists.append(List.of(
                "public EndPoint " + methodName + "() {",
                Formating.indent + "return EndPoint." + method + "(context, " + bookmarkAndCode(bookmark) + ", " + function + ");",
                "}"));
    }
    List<String> deleteEndpoint(String methodName, String method, BookmarkCodeAndUrlPattern bookmark, String function) {
        return Lists.append(List.of(
                "public EndPoint " + methodName + "() {",
                Formating.indent + "return EndPoint." + method + "(context, " + Strings.quote(bookmark.urlPattern) + ", " + function + ");",
                "}"));
    }
    List<String> createPutEndpoint(String methodName, String companionName, BookmarkCodeAndUrlPattern bookmark, String function, String stateFn) {
        return Lists.append(List.of(
                "public EndPoint " + methodName + "() {",
                Formating.indent + "return EndPoint.putEntity(" + companionName + ".companion, context, " + bookmarkAndCode(bookmark) + ", " + function + "," + stateFn + ");",
                "}"));
    }
    List<String> createPostEndpoint(String methodName, List<String> states, BookmarkCodeAndUrlPattern bookmark, String function, String stateFn) {
        return Lists.append(List.of(
                "public EndPoint " + methodName + "() {",
                Formating.indent + "return EndPoint.postEntity(context, " + bookmarkAndCode(bookmark) + ", " + "List.of(" + Lists.mapJoin(states, ",", Strings::quote) + ")," + function + "," + stateFn + ");",
                "}"));
    }
    List<String> createEntityEndpoint(ServerDom serverDom) {
        return List.of(
                "//A compilation error here might be because you haven't added a maven dependency to the 'core' jar",
                "public EndPoint entityEndpoint(){ return EndPointFactorys.<J>entityEndpointFromContext(context,entityCompanions());}",
                "  public EndPoint entityCodeEndpoint(){ return  EndPoint.javascript(context, \"{host}/resource/code\");}");
    }
    List<String> createCodeEndpoint(ServerDom serverDom) {
        return Lists.map(serverDom.codeDom.servedresourceDoms, rd -> "public EndPoint " + rd.entityNames.serverEntity.className +
                "codeEndpoint() {return EndPoint.javascript(context, " + Strings.quote(rd.bookmark.get().code) + ");}");
    }

    List<String> createEndpoints(ServerDom serverDom) {
        return Lists.flatMap(serverDom.codeDom.resourceDoms, ed -> {
            String className = ed.entityNames.serverEntity.className;
            String controllerName = ed.entityNames.serverController.className;
            String companionName = ed.entityNames.serverCompanion.asString();
            return Optionals.fold(ed.bookmark, () -> List.of(), b -> Lists.<String>append(
                    List.of("//EntityDom: " + ed.bookmark),
                    Optionals.flatMap(ed.actionsDom.createDom, dom -> createEndpointWithStateFn("createWithId" + className, "createEntityWithId", b, controllerName + "::createWithId", controllerName + "::stateFn")),
                    Optionals.flatMap(ed.actionsDom.createWithoutIdDom, dom -> createWithNoIdEndpoint("create" + className, "createEntity", b.withUrl(dom.path), controllerName + "::createWithoutIdRequestFrom", controllerName + "::createWithoutId", controllerName + "::stateFn")),
                    Optionals.flatMap(ed.actionsDom.putDom, dom -> createPutEndpoint("put" + className, companionName, b, controllerName + "::put", controllerName + "::stateFn")),
                    Optionals.flatMap(ed.actionsDom.getDom, dom -> dom.mustExist ?
                            createEndpointWithStateFn("get" + className, "getEntity", b,
                                    controllerName + "::get", controllerName + "::stateFn") :
                            createEndpointWithStateFn("getOptional" + className, "getOptionalEntity", b,
                                    controllerName + "::getOptional", controllerName + "::stateFn")),
                    Optionals.flatMap(ed.actionsDom.deleteDom, dom -> deleteEndpoint("delete" + className, "deleteEntity", b, controllerName + "::delete")),
                    Lists.flatMap(ed.actionsDom.postDoms, dom -> createPostEndpoint(dom.action + className, dom.states, b.withMoreUrl(dom.path), controllerName + "::" + dom.action, controllerName + "::stateFn"))));
        });
    }
    @Override public Result<String, FileDefn> apply(ServerDom serverDom) {
        String result = Lists.join(Lists.append(
                Formating.javaFile(getClass(), false, serverDom.originalDefn, "class", serverDom.serverName, "<J> implements IXingYiServer",
                        List.of("one.xingyi.server.EndPointFactorys  /* A compile error here might be because you haven't included the maven dependency for 'org.xingyi.server' */"),
                        XingYiGenerated.class, EndPoint.class, List.class, Lists.class, EndpointConfig.class, EndpointContext.class, IXingYiServer.class,
                        ExecutorService.class, SimpleServer.class, Executors.class, EndpointHandler.class, IXingYiServerCompanion.class,
                        JsonValue.class, JsonWriter.class, Files.class, HasBookmarkAndUrl.class),
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
