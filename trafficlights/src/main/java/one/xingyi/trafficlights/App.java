package one.xingyi.trafficlights;
import one.xingyi.core.endpoints.EndpointConfig;
import one.xingyi.core.marshelling.JsonValue;
import one.xingyi.core.utils.Lists;

import java.util.Objects;


public class App {
    public static void main(String[] args) {
        TrafficLightServer<JsonValue> trafficLightServer = new TrafficLightServer<>(EndpointConfig.defaultConfigNoParser, new TrafficLightsController());
        trafficLightServer.simpleServer(9000).start();
        System.out.println("Started traffic lights");
        System.out.println(Lists.mapJoin(trafficLightServer.endpoint().description(), "\n", Objects::toString));
    }
}
