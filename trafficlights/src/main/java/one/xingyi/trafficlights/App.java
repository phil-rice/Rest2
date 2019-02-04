package one.xingyi.trafficlights;
import one.xingyi.core.endpoints.EndpointConfig;
import one.xingyi.core.server.EndpointHandler;
import one.xingyi.core.server.HttpUtils;
import one.xingyi.core.server.SimpleServer;
import one.xingyi.core.utils.Lists;
import one.xingyi.json.Json;

import java.util.Objects;


public class App {
    public static void main(String[] args) {
        Json json = new Json();
        EndpointConfig<Object> defaultConfigNoParser = EndpointConfig.defaultConfig(json);
        one.xingyi.trafficlights.TrafficLightServer<Object> trafficLightServer = new one.xingyi.trafficlights.TrafficLightServer<>(defaultConfigNoParser, new TrafficLightsController(json));
        new SimpleServer(HttpUtils.makeDefaultExecutor(), new EndpointHandler(trafficLightServer.endpoint()), 9000).start();
        System.out.println("Started traffic lights: " + trafficLightServer.lens());
        System.out.println(Lists.mapJoin(trafficLightServer.endpoint().description(), "\n", Objects::toString));
    }
}
