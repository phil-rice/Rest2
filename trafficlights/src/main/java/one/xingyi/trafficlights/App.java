package one.xingyi.trafficlights;
import one.xingyi.core.endpoints.EndPoint;
import one.xingyi.core.endpoints.EndpointConfig;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.marshelling.JsonObject;
import one.xingyi.core.server.EndpointHandler;
import one.xingyi.core.server.HttpUtils;
import one.xingyi.core.server.SimpleServer;

import java.util.function.Function;


public class App {
    public static void main(String[] args) {
        TrafficLightServer<JsonObject> server = new TrafficLightServer<>(EndpointConfig.defaultConfig, new TrafficLightsController());
        SimpleServer simpleServer = new SimpleServer(HttpUtils.makeDefaultExecutor(), new EndpointHandler(EndPoint.compose(server.allEndpoints())), 9000);
        simpleServer.start();
        System.out.println("Started traffic lights");
    }
}
