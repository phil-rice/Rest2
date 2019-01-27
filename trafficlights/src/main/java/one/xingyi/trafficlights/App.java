package one.xingyi.trafficlights;
import one.xingyi.core.endpoints.EndPoint;
import one.xingyi.core.endpoints.EndpointConfig;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.marshelling.JsonObject;
import one.xingyi.core.server.EndpointHandler;
import one.xingyi.core.server.HttpUtils;
import one.xingyi.core.server.SimpleServer;
import one.xingyi.core.utils.Lists;

import java.util.Objects;
import java.util.function.Function;


public class App {
    public static void main(String[] args) {
        TrafficLightServer<JsonObject> trafficLightServer = new TrafficLightServer<>(EndpointConfig.defaultConfig, new TrafficLightsController());
        trafficLightServer.simpleServer(9000).start();
        System.out.println("Started traffic lights");
        System.out.println(Lists.mapJoin(trafficLightServer.endpoint().description(), "\n", Objects::toString));
    }
}
