package one.xingyi.trafficlights;
import lombok.val;
import one.xingyi.core.endpoints.EndpointConfig;
import one.xingyi.core.endpoints.EndpointContext;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.http.ServiceResponse;
import one.xingyi.core.javascript.JavascriptDetailsToString;
import one.xingyi.core.marshelling.DataAndJavaScript;
import one.xingyi.core.marshelling.IXingYiResponseSplitter;
import one.xingyi.core.marshelling.JsonObject;
import one.xingyi.core.marshelling.JsonWriter;
import one.xingyi.core.utils.BiConsumerWithException;
import one.xingyi.core.utils.Files;
import one.xingyi.trafficlights.server.domain.TrafficLights;
import org.junit.Test;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
public class TrafficLightTest {

    EndpointConfig<JsonObject> config = new EndpointConfig<>(Files.getText("header.js"), JsonWriter.cheapJson, "http://", JavascriptDetailsToString.simple);
    public void setup(BiConsumerWithException<TrafficLightsController, TrafficLightServer<JsonObject>> consumer) throws Exception {
        TrafficLightsController controller = new TrafficLightsController();
        consumer.accept(controller, new TrafficLightServer<>(config, controller));
    }

    ServiceRequest sr(String method, String uri) {return new ServiceRequest(method, uri, List.of(), "");}

    public void populate(TrafficLightsController controller, String id, String light) {
        controller.lights.put(id, new TrafficLights(id, light));

    }

    void checkSr(int statusCode, String json, ServiceResponse serviceResponse) {
        assertEquals(statusCode, serviceResponse.statusCode);
        String body = serviceResponse.body;
        DataAndJavaScript dataAndJavaScript = IXingYiResponseSplitter.splitter.apply(serviceResponse);
        assertTrue(body, body.startsWith(config.rootJavascript));
        assertEquals(json, dataAndJavaScript.data);
    }

    void checkSrNotFound(ServiceResponse serviceResponse) {
        assertEquals(404, serviceResponse.statusCode);
    }

    @Test
    public void testGetOptional() throws Exception {
        setup((controller, server) -> {
            populate(controller, "someId", "red");
            checkSr(200, "{\"id\":\"someId\",\"color\":\"red\"}", server.getOptionalTrafficLights().apply(sr("get", "/lights/someId")).get().get());
            checkSrNotFound(server.getOptionalTrafficLights().apply(sr("get", "/lights/someNotInId")).get().get());
        });
    }
}
