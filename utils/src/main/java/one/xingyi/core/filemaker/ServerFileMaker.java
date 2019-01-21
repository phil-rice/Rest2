package one.xingyi.core.filemaker;
import one.xingyi.core.annotations.XingYiGenerated;
import one.xingyi.core.codeDom.ServerDom;
import one.xingyi.core.utils.Formating;
import one.xingyi.core.utils.Lists;

import java.util.List;
public class ServerFileMaker implements IFileMaker<ServerDom> {

//        @XingYiGenerated
//        public static EntityRegister register = SimpleEntityRegister.simple(EntityServerCompanion.companion,
//                one.xingyi.restExample.AddressServerCompanion.companion,
//                one.xingyi.restExample.PersonServerCompanion.companion,
//                one.xingyi.restExample.TelephoneNumberServerCompanion.companion);;
////anything with urlPattern set in it should appear as a parameter
////If you have a compilation error check you've done a full compile: there is an issue with the annotation processors
//@XingYiGenerated
//public static <J> EndPoint createEndpoints(JsonTC<J> jsonTC,IEntityRead<Address> address,IEntityRead<Person> person) {
//        EndPoint entityDetailsEndPoint = EntityDetailsEndpoint.entityDetailsEndPoint(jsonTC, register);
//        EndPoint getAddressEndpoint = GetEntityEndpoint.getOptionalEndPoint(jsonTC, register, one.xingyi.restExample.AddressServerCompanion.companion, address::read);
//        EndPoint getPersonEndpoint = GetEntityEndpoint.getOptionalEndPoint(jsonTC, register, one.xingyi.restExample.PersonServerCompanion.companion, person::read);
//        return EndPoint.compose(entityDetailsEndPoint,getAddressEndpoint,getPersonEndpoint);
//        }
//

    private List<String> generateRegister(ServerDom serverDom) {
        return List.of(
                "@XingYiGenerated public static  EntityRegister register = SimpleEntityRegister.simple(one.xingyi.core.httpClient.server.companion.EntityCompanion{",
                Lists.mapJoin(serverDom.codeDom.entityDoms, ",", ed -> ed.entityName.serverCompanion.asString() + ".companion"),
                "}");
    }
    private List<String> createEndPoints(ServerDom serverDom) {
        return List.of();
    }
    @Override public FileDefn apply(ServerDom serverDom) {
        String result = Lists.join(Lists.append(
                Formating.javaFile(getClass(), "class", serverDom.serverName, "",
                        List.of(), XingYiGenerated.class),
//                Formating.indent(generateRegister(serverDom)),
                Formating.indent(createEndPoints(serverDom)),
                List.of("/*" + serverDom + "*/"),
                List.of("}")
        ), "\n");
        return new FileDefn(serverDom.serverName, result);
    }
}
