package one.xingyi.core.filemaker;
import one.xingyi.core.annotations.XingYiGenerated;
import one.xingyi.core.codeDom.ServerDom;
import one.xingyi.core.endpoints.EndPoint;
import one.xingyi.core.endpoints.EndpointAcceptor1;
import one.xingyi.core.endpoints.EndpointConfig;
import one.xingyi.core.endpoints.EndpointContext;
import one.xingyi.core.marshelling.JsonObject;
import one.xingyi.core.marshelling.JsonWriter;
import one.xingyi.core.sdk.IXingYiServerCompanion;
import one.xingyi.core.server.EndpointHandler;
import one.xingyi.core.server.SimpleServer;
import one.xingyi.core.utils.*;
import one.xingyi.core.validation.Result;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
//                List.of("public static <J> EndPoint entityEndpoints(EndpointConfig<J> config) { return EndPointFactorys.create(config, List.of(",
//                        Lists.join(Formating.indent(Lists.map(serverDom.defnNames,
//                                n -> "new GetEntityEndpointDetails<>(" + n.entityNames.serverCompanion.asString() + ".companion, new " + n.getName + "())")),
//                                ",\n" + Formating.indent) + "),"),
//                List.of("List.of(",
//                        Lists.join(Formating.indent(Lists.map(serverDom.codeDom.entityDoms, ed -> ed.entityNames.serverCompanion.asString() + ".companion")), ",\n" + Formating.indent),
//                        "));}"));
//        //    static EndPoint entityEndpoints = EndPointFactorys.create(config,
//        //            List.of(
//        //                    new GetEntityEndpointDetails<>(PersonCompanion.companion, new PersonGet()),
//        //                    new GetEntityEndpointDetails<>(AddressCompanion.companion, new AddressGet())),
//        //            List.of(TelephoneNumberCompanion.companion));
//    }

    List<String> makeCompanions(ServerDom serverDom) {
        return List.of(
                "public List<IXingYiServerCompanion<?, ?>> companions() {return List.of(",
                Formating.indent + Lists.mapJoin(serverDom.codeDom.entityDoms, ",", ed -> ed.entityNames.serverCompanion.asString()) + ");}");
    }

    List<String> createFields(ServerDom serverDom) {
        return Lists.<String>append(
                List.of("final EndpointContext<J> context;"),
                Lists.map(serverDom.codeDom.entityDoms, ed -> ed.entityNames.serverController.asString() + " " + ed.entityNames.serverController.className + ";")
        );
    }
    List<String> createConstructor(ServerDom serverDom) {
        return Lists.<String>append(
                List.of("public " + serverDom.serverName.className + "(EndpointConfig<J> config," +
                                Lists.mapJoin(serverDom.codeDom.entityDoms, ",", ed -> ed.entityNames.serverController.asVariableDeclaration()) + "){",
                        Formating.indent + "this.context = config.from(companions());"),
                Formating.indent(Lists.map(serverDom.codeDom.entityDoms, ed -> "this." + ed.entityNames.serverController.className + " = " + ed.entityNames.serverController.className + ";")),
                List.of("}")
        );
    }

    List<String> createCompanions(ServerDom serverDom) {
        return List.of(
                "public List<IXingYiServerCompanion<?, ?>> companions(){return List.of(",
                Formating.indent + Lists.mapJoin(serverDom.codeDom.entityDoms, ",", ed -> ed.entityNames.serverCompanion.asString() + ".companion"),
                ");}");
    }

    List<String> createEndpoint(String methodName, String method, String bookmark, String function) {
        return Lists.append(List.of("public EndPoint " + methodName + "() {"),
                Formating.indent(List.of(
                        "return EndPoint.javascriptAndJson(context.jsonTC, 200, context.protocol,",
                        "EndpointAcceptor1.<String>bookmarkAcceptor(" + Strings.quote(method) + ", " + Strings.quote(bookmark) + ", (sr, s) -> s),",
                        function + ", context.javascriptStore);")),
                List.of("}"));
    }
    List<String> createEndpoints(ServerDom serverDom) {
        return Lists.flatMap(serverDom.codeDom.entityDoms, ed -> {
            String className = ed.entityNames.serverController.className;
            return Optionals.fold(ed.bookmark, () -> List.of(), b -> Lists.<String>append(
                    List.of("//EntityDom: " + ed.bookmark),
                    Optionals.flatMap(ed.actionsDom.getDom, dom -> createEndpoint("get" + className, "get", b.urlPattern, className + "::get")),
//                    Optionals.flatMap(ed.actionsDom.putDom, dom -> createEndpoint("put" + className, "put", b.urlPattern, className + "::put")),
//                    Optionals.flatMap(ed.actionsDom.deleteDom, dom -> createEndpoint("delete" + className, "delete", b.urlPattern, className + "::delete")),
                    Optionals.flatMap(ed.actionsDom.createDom, dom -> createEndpoint("create" + className, "create", b.urlPattern, className + "::create")
//                    Optionals.flatMap(ed.actionsDom.createWithoutIdDom, dom -> "//for createWithoutIdDom " + dom),
                    )));
        });


    }
    @Override public Result<String, FileDefn> apply(ServerDom serverDom) {
        String result = Lists.join(Lists.append(
                Formating.javaFile(getClass(), serverDom.originalDefn, "class", serverDom.serverName, "<J>",
                        List.of("one.xingyi.server.EndPointFactorys", "one.xingyi.server.GetEntityEndpointDetails"),
                        XingYiGenerated.class, EndPoint.class, List.class, Lists.class, EndpointConfig.class, EndpointContext.class,
                        ExecutorService.class, SimpleServer.class, Executors.class, EndpointHandler.class, IXingYiServerCompanion.class,
                        JsonObject.class, JsonWriter.class, Files.class, EndpointAcceptor1.class),
//                Formating.indent(generateRegister(serverDom)),
                Formating.indent(createFields(serverDom)),
                Formating.indent(createConstructor(serverDom)),
                Formating.indent(createCompanions(serverDom)),
                Formating.indent(createEndpoints(serverDom)),
//                Formating.indent(makeSimpleServer(serverDom)),
                List.of("/*" + serverDom),
//                List.of("//gets"),
//                Formating.indent(Lists.map(serverDom.defnNames, Objects::toString)),
                List.of("//entities"),
                Formating.indent(Lists.map(serverDom.codeDom.entityDoms, Objects::toString)),
                List.of("*/"),
                List.of("}")
        ), "\n");
        return Result.succeed(new FileDefn(serverDom.serverName, result));
    }
}
