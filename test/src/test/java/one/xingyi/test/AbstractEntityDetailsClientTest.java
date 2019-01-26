package one.xingyi.test;
//

import one.xingyi.core.endpoints.EndPoint;
import one.xingyi.core.endpoints.EndpointConfig;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.http.ServiceResponse;
import one.xingyi.core.httpClient.HttpService;
import one.xingyi.core.httpClient.client.companion.UrlPatternCompanion;
import one.xingyi.core.httpClient.client.view.UrlPattern;
import one.xingyi.core.javascript.JavascriptDetailsToString;
import one.xingyi.core.marshelling.JsonObject;
import one.xingyi.core.marshelling.JsonWriter;
import one.xingyi.core.marshelling.UnexpectedResponse;
import one.xingyi.core.utils.Files;
import one.xingyi.reference.address.AddressGet;
import one.xingyi.reference.address.client.view.AddressLine12View;
import one.xingyi.reference.address.server.companion.AddressCompanion;
import one.xingyi.reference.person.PersonGet;
import one.xingyi.reference.person.client.view.PersonNameView;
import one.xingyi.reference.person.server.companion.PersonCompanion;
import one.xingyi.reference.telephone.server.companion.TelephoneNumberCompanion;
import one.xingyi.server.EndPointFactorys;
import one.xingyi.server.GetEntityEndpointDetails;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
abstract class AbstractEntityDetailsClientTest {

    abstract protected Function<ServiceRequest, CompletableFuture<ServiceResponse>> httpClient();
    abstract protected String expectedHost();

    static EndpointConfig<JsonObject> config = new EndpointConfig<>(Files.getText("header.js"), JsonWriter.cheapJson, "http://", JavascriptDetailsToString.simple);

    static EndPoint entityEndpoints = EndPointFactorys.<JsonObject>create(config,
            List.of(
                    new GetEntityEndpointDetails<>(PersonCompanion.companion, new PersonGet()),
                    new GetEntityEndpointDetails<>(AddressCompanion.companion, new AddressGet())),
            List.of(TelephoneNumberCompanion.companion));

//    static EndPoint entityEndpoints = PersonServer.entityEndpoints(config);

    HttpService rawService;
    HttpService service() { if (rawService == null) rawService = HttpService.defaultService("http://localhost:9000", httpClient()); return rawService; }

    @Test
    public void testGetPrimitive() throws ExecutionException, InterruptedException {
        assertEquals(expectedHost() + "/person/{id}", service().primitiveGet(UrlPatternCompanion.companion, "/person", UrlPattern::urlPattern).get());
        assertEquals(expectedHost() + "/person/{id}", UrlPattern.getPrimitive(service(), "/person", e -> e.urlPattern()).get());
    }


    @Test
    public void testGetUrlPattern() throws ExecutionException, InterruptedException {
        assertEquals(expectedHost() + "/person/{id}", PersonNameView.getUrlPattern(service()).get());
        assertEquals(expectedHost() + "/address/{id}", AddressLine12View.getUrlPattern(service()).get());
    }
    @Test
    public void testGetUrlPatternWhenEntityNotRegistered() throws ExecutionException, InterruptedException {
        try {
            UrlPattern.getPrimitive(service(), "/notin", UrlPattern::urlPattern).get();
            fail();
        } catch (Exception e) {
            Throwable cause = e.getCause().getCause();
            assertTrue(cause.getClass().getName(), cause instanceof UnexpectedResponse);
            assertEquals(404, ((UnexpectedResponse) cause).response.statusCode);
        }
    }

    @Test
    public void testGetPerson() throws ExecutionException, InterruptedException {
        assertEquals("someName", PersonNameView.get(service(), "id1", PersonNameView::name).get());
    }

//    @Test
//    public void testGetAddress() throws ExecutionException, InterruptedException {
//        assertEquals(Optional.of(address), addressStore.read("add1").get());
//        assertEquals("{'line1':'someLine1','line2':'someLine2'}".replace('\'', '"'),
//                IXingYiResponseSplitter.splitter.apply(entityEndpoints.apply(new ServiceRequest("get", "/address/add1", Arrays.asList(), "")).get().get()).data);
//
//        assertEquals("someLine1", client.get(IAddressLine12.class, "add1", IAddressLine12::line1).get());
//    }
//
//    static final String name = EntityClientImpl.class.getName();
//
//    @Test
//    public void testWithMultipleInterfaces() throws ExecutionException, InterruptedException {
//        assertEquals("serverName/one.xi", client.primitiveGet(ITestMultiple.class, "http://localhost:9000/person/id1", e -> e.name() + "/" + e.address().toString().substring(0, 6)).get());
//        assertEquals("serverName/one.xi", client.get(ITestMultiple.class, "id1", e -> e.name() + "/" + e.address().toString().substring(0, 6)).get());
//    }
//    @Test
//    public void testWithMultipleInterfaces2() throws ExecutionException, InterruptedException {
////    Thread.sleep(100000);
//        assertEquals("serverName/one.xi", client.primitiveGet(ITestMultiple.class, "http://localhost:9000/person/id1", e -> e.name() + "/" + e.address().toString().substring(0, 6)).get());
//        //solution to this is to have a @XingYiMulti annotation and apply instance which can delegate. Actually pretty straightforwards...
//    }
//
//    @Test
//    public void testCanGetDependent() throws ExecutionException, InterruptedException {
//        assertEquals("someLine1", client.get(IPersonAddress.class, "id1", p -> p.address().line1()).get());
//        assertEquals("someLine1", client.get(ITestMultiple.class, "id1", p -> p.address().line1()).get());
//    }
}
