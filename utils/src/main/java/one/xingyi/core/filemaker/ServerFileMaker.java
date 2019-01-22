package one.xingyi.core.filemaker;
import one.xingyi.core.annotations.XingYiGenerated;
import one.xingyi.core.codeDom.ServerDom;
import one.xingyi.core.endpoints.EndPoint;
import one.xingyi.core.endpoints.EndpointConfig;
import one.xingyi.core.utils.Formating;
import one.xingyi.core.utils.Lists;

import java.util.List;
import java.util.Objects;
public class ServerFileMaker implements IFileMaker<ServerDom> {


    private List<String> createEndPoints(ServerDom serverDom) {
        return Lists.append(
                List.of("public static <J> EndPoint entityEndpoints(EndpointConfig<J> config) { return EndPointFactorys.create(config, List.of(",
                        Lists.join(Formating.indent(Lists.map(serverDom.defnNames,
                                n -> "new GetEntityEndpointDetails<>(" + n.entityNames.serverCompanion.asString() + ".companion, new " + n.getName + "())")),
                                ",\n" + Formating.indent) + "),"),
                List.of("List.of(",
                        Lists.join(Formating.indent(Lists.map(serverDom.codeDom.entityDoms, ed -> ed.entityName.serverCompanion.asString() + ".companion")), ",\n" + Formating.indent),
                        "));}"));
        //    static EndPoint entityEndpoints = EndPointFactorys.create(config,
        //            List.of(
        //                    new GetEntityEndpointDetails<>(PersonCompanion.companion, new PersonGet()),
        //                    new GetEntityEndpointDetails<>(AddressCompanion.companion, new AddressGet())),
        //            List.of(TelephoneNumberCompanion.companion));
    }
    @Override public FileDefn apply(ServerDom serverDom) {
        String result = Lists.join(Lists.append(
                Formating.javaFile(getClass(), serverDom.originalDefn, "class", serverDom.serverName, "",
                        List.of("one.xingyi.server.EndPointFactorys", "one.xingyi.server.GetEntityEndpointDetails"),
                        XingYiGenerated.class, EndPoint.class, List.class, Lists.class, EndpointConfig.class),
//                Formating.indent(generateRegister(serverDom)),
                Formating.indent(createEndPoints(serverDom)),
                List.of("/*" + serverDom),
                List.of("//gets"),
                Formating.indent(Lists.map(serverDom.defnNames, Objects::toString)),
                List.of("//entities"),
                Formating.indent(Lists.map(serverDom.codeDom.entityDoms, Objects::toString)),
                List.of("*/"),
                List.of("}")
        ), "\n");
        return new FileDefn(serverDom.serverName, result);
    }
}
