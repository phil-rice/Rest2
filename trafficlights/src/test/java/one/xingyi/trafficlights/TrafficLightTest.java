package one.xingyi.trafficlights;
import one.xingyi.core.endpoints.EndPoint;
import one.xingyi.core.endpoints.EndpointConfig;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.http.ServiceResponse;
import one.xingyi.core.httpClient.HttpServiceCompletableFuture;
import one.xingyi.core.marshelling.DataToBeSentToClient;
import one.xingyi.core.marshelling.IXingYiResponseSplitter;
import one.xingyi.core.utils.Consumer3WithException;
import one.xingyi.core.utils.DigestAndString;
import one.xingyi.json.Json;
import one.xingyi.trafficlights.client.view.ColourView;
import one.xingyi.trafficlights.client.view.LocationView;
import one.xingyi.trafficlights.server.domain.TrafficLights;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

public class TrafficLightTest {

    Json jsonParserAndWriter = new Json();
    EndpointConfig<Object> config = EndpointConfig.defaultConfig(jsonParserAndWriter);

    public void setup(Consumer3WithException<TrafficLightsController<Object>, one.xingyi.trafficlights.TrafficLightServer<Object>, HttpServiceCompletableFuture> consumer) throws Exception {
        TrafficLightsController controller = new TrafficLightsController(jsonParserAndWriter);
        one.xingyi.trafficlights.TrafficLightServer<Object> server = new one.xingyi.trafficlights.TrafficLightServer<>(config, controller);
        HttpServiceCompletableFuture service = HttpServiceCompletableFuture.defaultService("http://somehost", EndPoint.toKliesli(EndPoint.printlnLog(server.endpoint())));
        consumer.accept(controller, server, service);
    }

    ServiceRequest sr(String method, String uri) {return new ServiceRequest(method, uri, List.of(), "");}

    public void populate(TrafficLightsController controller, String id, String light, String location) {
        controller.store.put(id, new TrafficLights(id, light, location));

    }

    void checkSr(TrafficLightServer server, int statusCode, String json, ServiceResponse serviceResponse) {
        assertEquals(statusCode, serviceResponse.statusCode);
        String body = serviceResponse.body;
        DataToBeSentToClient dataToBeSentToClient = IXingYiResponseSplitter.rawSplit(serviceResponse);
        DigestAndString digestAndString = server.context.javascriptStore.findDigestAndString(List.of());
        assertTrue(body, body.contains(digestAndString.digest+IXingYiResponseSplitter.marker));
        assertEquals(json, dataToBeSentToClient.data);
    }

    void checkSrNotFound(ServiceResponse serviceResponse) {
        assertEquals(404, serviceResponse.statusCode);
    }

    @Test
    public void testCanGetEntity() throws Exception {
        setup((controller, server, service) -> {
            checkSr(server,200, "{\"urlPattern\":\"/lights/{id}\"}", server.entityEndpoint().apply(sr("get", "/lights")).get().get());
            checkSr(server,200, "{\"urlPattern\":\"/lights/{id}\"}", server.endpoint().apply(sr("get", "/lights")).get().get());
            checkSr(server,200, "{\"urlPattern\":\"/lights/{id}\"}", server.entityEndpoint().apply(sr("get", "http://somehost/lights")).get().get());
        });
    }

    @Test
    public void testGetOptionalEndpoint() throws Exception {
        setup((controller, server, service) -> {
            populate(controller, "someId", "red", "someLocation");
            checkSr(server,200, "{\"id\":\"someId\",\"color\":\"red\",\"location\":\"someLocation\",\"links_\":[{\"_self\":\"/lights/someId\"},{\"orange\":\"{host}/lights/{id}/orange\"}]}",
                    server.getOptionalTrafficLights().apply(sr("get", "/lights/someId")).get().get());
            checkSrNotFound(server.getOptionalTrafficLights().apply(sr("get", "/lights/someNotInId")).get().get());
        });
    }
    @Test
    public void testGetOptionalEndpointUsingAllEndpoints() throws Exception {
        setup((controller, server, service) -> {
            populate(controller, "someId", "red", "someLocation");
            checkSr(server,200, "{\"id\":\"someId\",\"color\":\"red\",\"location\":\"someLocation\",\"links_\":[{\"_self\":\"/lights/someId\"},{\"orange\":\"{host}/lights/{id}/orange\"}]}", server.endpoint().apply(sr("get", "/lights/someId")).get().get());
            checkSrNotFound(server.getOptionalTrafficLights().apply(sr("get", "/lights/someNotInId")).get().get());
        });
    }
    @Test
    public void testGetFromView() throws Exception {
        setup((controller, server, service) -> {
            populate(controller, "someId", "red", "someLocation");
            assertEquals("someIdred", ColourView.get(service, "someId", v -> v.id() + v.color()).get());
        });
    }

    @Test public void testCanCreateThenGet() throws Exception {
        setup((controller, server, service) -> {
//            System.out.println(Lists.join(server.endpoint().description(),"\n"));
            Function<ColourView, String> fn = c -> c.id() + c.color();
//            assertEquals("1red", ColourView.create(service, "1").thenApply(fn).get());
            assertEquals("3red", ColourView.create(service, "3").thenApply(fn).get());
            assertEquals("2red", ColourView.create(service, "2").thenApply(fn).get());

            assertEquals("1red", ColourView.get(service, "1", fn).get());
            assertEquals("2red", ColourView.get(service, "2", fn).get());
        });
    }

    @Test public void testCanCreateWithoutId() throws Exception {
        setup((controller, server, service) -> {
            populate(controller, "someId", "red", "someLocation");
            ColourView prototype = ColourView.get(service, "someId", x -> x).get();
            assertEquals("somered", ColourView.create(service, prototype.withid("some")).thenApply(idV -> idV.id + idV.t.color()).get());
            assertEquals("othergreen", ColourView.create(service, prototype.withid("other").withcolor("green")).thenApply(idV -> idV.id + idV.t.color()).get());
        });
    }


    @Test public void testCanGetOptional() throws Exception {
        setup((controller, server, service) -> {
            Function<ColourView, String> fn = c -> c.id() + c.color();
            assertEquals(Optional.of("1red"), ColourView.getOptional(service, "1", fn).get());
            assertEquals(Optional.empty(), ColourView.getOptional(service, "2", fn).get());
        });
    }

    @Test public void testCanDelete() throws Exception {
        setup((controller, server, service) -> {
            populate(controller, "someId", "red", "someLocation");
            assertTrue(controller.store.containsKey("1"));
            assertEquals(Boolean.TRUE, ColourView.delete(service, "1").get());
            assertFalse(controller.store.containsKey("1"));
        });
    }

    @Test public void testCanEdit() throws Exception {
        setup((controller, server, service) -> {


            populate(controller, "someId", "red", "someLocation");
            assertEquals("newLocation", LocationView.edit(service, "someId", loc -> loc.withlocation("newLocation")).get().location());
            TrafficLights lights = controller.store.get("someId");
            assertEquals("newLocation", lights.location());
            assertEquals("newLocation", LocationView.get(service, "someId", v -> v.location()).get());
        });
    }

    @Test @Ignore public void testTheStateChangeMethods() throws Exception {
        setup((controller, server, service) -> {
            //lets think about this programming model in a bit... it's important but off topic
//            ColourView.getFor(service, "someId", new Pretend<ColourView>()
            //The optional is because the entity might not be in the right state
//            CompletableFuture<Optional<ColourView>> doneIt = ColourView.orange(service, "1");
//    class Pretend<T> {
//        Optional<Function<T, T>> processRed;
//        Optional<Function<T, T>> processOrange;
//        Optional<Function<T, T>> processGreen;
//        Optional<Function<T, T>> processFlashing;
//    }

            fail();
        });
    }

}
