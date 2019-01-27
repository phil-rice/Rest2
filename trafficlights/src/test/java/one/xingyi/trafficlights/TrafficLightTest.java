package one.xingyi.trafficlights;
import one.xingyi.core.endpoints.EndPoint;
import one.xingyi.core.endpoints.EndpointConfig;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.http.ServiceResponse;
import one.xingyi.core.httpClient.HttpService;
import one.xingyi.core.javascript.JavascriptDetailsToString;
import one.xingyi.core.marshelling.DataAndJavaScript;
import one.xingyi.core.marshelling.IXingYiResponseSplitter;
import one.xingyi.core.marshelling.JsonObject;
import one.xingyi.core.marshelling.JsonWriter;
import one.xingyi.core.utils.BiConsumerWithException;
import one.xingyi.core.utils.Files;
import one.xingyi.trafficlights.client.companion.ColourViewCompanion;
import one.xingyi.trafficlights.client.view.ColourView;
import one.xingyi.trafficlights.server.companion.TrafficLightsCompanion;
import one.xingyi.trafficlights.server.domain.TrafficLights;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
public class TrafficLightTest {

    EndpointConfig<JsonObject> config = EndpointConfig.defaultConfig;
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
    public void testCanGetEntity() throws Exception {
        setup((controller, server) -> {
            checkSr(200, "{\"urlPattern\":\"/lights/{id}\"}", server.entityEndpoint().apply(sr("get", "/lights")).get().get());
            checkSr(200, "{\"urlPattern\":\"/lights/{id}\"}", server.endpoint().apply(sr("get", "/lights")).get().get());
            checkSr(200, "{\"urlPattern\":\"/lights/{id}\"}", server.entityEndpoint().apply(sr("get", "http://somehost/lights")).get().get());
        });
    }

    @Test
    public void testGetOptionalEndpoint() throws Exception {
        setup((controller, server) -> {
            populate(controller, "someId", "red");
            checkSr(200, "{\"id\":\"someId\",\"color\":\"red\"}", server.getOptionalTrafficLights().apply(sr("get", "/lights/someId")).get().get());
            checkSrNotFound(server.getOptionalTrafficLights().apply(sr("get", "/lights/someNotInId")).get().get());
        });
    }
    @Test
    public void testGetOptionalEndpointUsingAllEndpoints() throws Exception {
        setup((controller, server) -> {
            populate(controller, "someId", "red");
            checkSr(200, "{\"id\":\"someId\",\"color\":\"red\"}", server.endpoint().apply(sr("get", "/lights/someId")).get().get());
            checkSrNotFound(server.getOptionalTrafficLights().apply(sr("get", "/lights/someNotInId")).get().get());
        });
    }
    @Test
    public void testGetFromView() throws Exception {
        setup((controller, server) -> {
            populate(controller, "someId", "red");
            HttpService service = HttpService.defaultService("http://somehost", EndPoint.toKliesli(server.endpoint()));
            assertEquals("someIdred", ColourView.get(service, "someId", v -> v.id() + v.color()).get());
        });
    }

    @Test public void testCanCreateThenGet() throws Exception {
        setup((controller, server) -> {
            HttpService service = HttpService.defaultService("http://somehost", EndPoint.toKliesli(server.endpoint()));
            Function<ColourView, String> fn = c -> c.id() + c.color();
            assertEquals("1red", ColourView.create(service, "1").thenApply(fn).get());
            assertEquals("2red", ColourView.create(service, "2").thenApply(fn).get());

            assertEquals("1red", ColourView.get(service, "1", fn).get());
            assertEquals("2red", ColourView.get(service, "2", fn).get());
        });
    }
    @Ignore @Test public void testCanCreateWithoutId() throws Exception {
        setup((controller, server) -> {
            populate(controller, "someId", "red");
            HttpService service = HttpService.defaultService("http://somehost", EndPoint.toKliesli(server.endpoint()));
            assertEquals("1red", ColourView.create(service).thenApply(idV -> idV.id + idV.t.color()).get());
            assertEquals("2red", ColourView.create(service).thenApply(idV -> idV.id + idV.t.color()).get());
            assertEquals("3red", ColourView.create(service).thenApply(idV -> idV.id + idV.t.color()).get());
        });
    }


    @Test public void testCanGetOptional() throws Exception {
        setup((controller, server) -> {
            HttpService service = HttpService.defaultService("http://somehost", EndPoint.toKliesli(server.endpoint()));
            Function<ColourView, String> fn = c -> c.id() + c.color();
            assertEquals(Optional.of("1red"), ColourView.getOptional(service, "1", fn).get());
            assertEquals(Optional.empty(), ColourView.getOptional(service, "2", fn).get());
        });
    }

    @Test public void testCanDelete() throws Exception {
        setup((controller, server) -> {
            populate(controller, "someId", "red");
            assertTrue(controller.lights.containsKey("1"));
            HttpService service = HttpService.defaultService("http://somehost", EndPoint.toKliesli(server.endpoint()));
            assertEquals(Boolean.TRUE, ColourView.delete(service, "1").get());
            assertFalse(controller.lights.containsKey("1"));
        });
    }

}
