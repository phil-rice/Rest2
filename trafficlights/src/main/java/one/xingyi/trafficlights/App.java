package one.xingyi.trafficlights;
import one.xingyi.core.endpoints.EndPoint;
import one.xingyi.core.endpoints.EndpointConfig;
import one.xingyi.server.EndPointFactorys;
import one.xingyi.server.GetEntityEndpointDetails;
import one.xingyi.trafficlights.operations.Gash;

import java.util.List;
public class App {


    //We can add a list of end point modifiers
    public static <J> EndPoint entityEndpoints(EndpointConfig<J> config) {
        return EndPointFactorys.create(config, List.of(
                new GetEntityEndpointDetails<>(one.xingyi.trafficlights.server.companion.TrafficLightsCompanion.companion, new one.xingyi.trafficlights.TrafficLightStore())),
                List.of(
                        one.xingyi.trafficlights.server.companion.TrafficLightsCompanion.companion
                ));
    }
    public static void main(String[] args) {
        TrafficLightStore store = new TrafficLightStore();
        Gash gash = new Gash(store); //this has lots of  end points, some of which are state aware
        //the idea is that these endpoints can be assessed for inclusion based on state

        TrafficLightServer.simple().start();
        System.out.println("Started traffic lights");
    }
}
