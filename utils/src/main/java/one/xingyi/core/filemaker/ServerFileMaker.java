package one.xingyi.core.filemaker;
import one.xingyi.core.annotations.XingYiGenerated;
import one.xingyi.core.codeDom.ServerDom;
import one.xingyi.core.endpoints.EndPoint;
import one.xingyi.core.endpoints.EndpointConfig;
import one.xingyi.core.marshelling.JsonObject;
import one.xingyi.core.marshelling.JsonWriter;
import one.xingyi.core.server.EndpointHandler;
import one.xingyi.core.server.SimpleServer;
import one.xingyi.core.utils.Files;
import one.xingyi.core.utils.Formating;
import one.xingyi.core.utils.Lists;
import one.xingyi.core.validation.Result;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class ServerFileMaker implements IFileMaker<ServerDom> {
    List<String> makeSimpleServer(ServerDom serverDom) {
        return List.of("public static SimpleServer simple(){",
                Formating.indent + "ExecutorService executorService = Executors.newFixedThreadPool(20);",
                Formating.indent + "EndpointConfig<JsonObject> config = new EndpointConfig<>(Files.getText(\"header.js\"), JsonWriter.cheapJson, \"http://\");",
                Formating.indent + "return  new SimpleServer(executorService, new EndpointHandler(entityEndpoints(config)), " + serverDom.port + ");",
                "}");
    }

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
    @Override public Result<String, FileDefn> apply(ServerDom serverDom) {
        String result = Lists.join(Lists.append(
                Formating.javaFile(getClass(), serverDom.originalDefn, "class", serverDom.serverName, "",
                        List.of("one.xingyi.server.EndPointFactorys", "one.xingyi.server.GetEntityEndpointDetails"),
                        XingYiGenerated.class, EndPoint.class, List.class, Lists.class, EndpointConfig.class,
                        ExecutorService.class, SimpleServer.class, Executors.class, EndpointHandler.class,
                        JsonObject.class, JsonWriter.class, Files.class),
//                Formating.indent(generateRegister(serverDom)),
//                Formating.indent(createEndPoints(serverDom)),
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
