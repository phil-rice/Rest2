package one.xingyi.test;
//

import one.xingyi.core.endpoints.EndPoint;
import one.xingyi.core.endpoints.EndpointConfig;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.http.ServiceResponse;
import one.xingyi.core.httpClient.HttpServiceCompletableFuture;
import one.xingyi.core.httpClient.client.companion.UrlPatternCompanion;
import one.xingyi.core.httpClient.client.view.UrlPattern;
import one.xingyi.core.marshelling.JsonValue;
import one.xingyi.core.marshelling.UnexpectedResponse;
import one.xingyi.core.utils.DigestAndString;
import one.xingyi.json.Json;
import one.xingyi.reference3.PersonServer;
import one.xingyi.reference3.person.PersonController;
import one.xingyi.reference3.person.client.companion.PersonNameViewCompanion;
import one.xingyi.reference3.person.client.view.PersonLine12View;
import one.xingyi.reference3.person.client.view.PersonNameView;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
abstract class AbstractResourceDetailsClientTest {

    abstract protected Function<ServiceRequest, CompletableFuture<ServiceResponse>> httpClient();
    abstract protected String expectedHost();

    static Json json = new Json();
    static EndpointConfig<Object> config = EndpointConfig.defaultConfig(json, json);


    static PersonServer<Object> server = new PersonServer<>(config, new PersonController());
    static EndPoint entityEndpoints = EndPoint.compose(server.allEndpoints());

//    static EndPoint entityEndpoints = PersonServer.entityEndpoints(config);

    HttpServiceCompletableFuture rawService;
    HttpServiceCompletableFuture service() { if (rawService == null) rawService = HttpServiceCompletableFuture.defaultService("http://localhost:9000", httpClient()); return rawService; }

    @Test
    public void testGetPrimitive() throws ExecutionException, InterruptedException {
        assertEquals(expectedHost() + "/person/{id}", service().primitive(UrlPatternCompanion.companion, "get", "/person", UrlPattern::urlPattern).get());
        assertEquals(expectedHost() + "/person/{id}", UrlPatternCompanion.companion.primitive(service(), "get", "/person", e -> e.urlPattern()).get());
    }

    @Test
    public void testGetJavascript() throws ExecutionException, InterruptedException {
        DigestAndString digestAndString = server.context.javascriptStore.findDigestAndString(List.of());
        ServiceRequest sr = new ServiceRequest("get", expectedHost() + "/person/code/" + digestAndString.digest, List.of(), "");
        ServiceResponse serviceResponse = httpClient().apply(sr).get();
        assertEquals(serviceResponse.toString(),200, serviceResponse.statusCode);
        assertEquals(digestAndString.string, serviceResponse.body);
    }


    @Test
    public void testGetUrlPattern() throws ExecutionException, InterruptedException {
        assertEquals(expectedHost() + "/person/{id}", PersonNameViewCompanion.companion.getUrlPattern(service()).get());
//        assertEquals(expectedHost() + "/address/{id}", AddressLine12View.getUrlPattern(service()).get());
    }
    @Test
    public void testGetUrlPatternWhenEntityNotRegistered() throws ExecutionException, InterruptedException {
        try {
            UrlPatternCompanion.companion.primitive(service(), "get", "/notin", UrlPattern::urlPattern).get();
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
//                IXingYiResponseSplitter.inLineOnlySplitter.apply(entityEndpoints.apply(new ServiceRequest("get", "/address/add1", Arrays.asList(), "")).get().get()).data);
//
//        assertEquals("someLine1", client.get(IAddressLine12.class, "add1", IAddressLine12::line1).get());
//    }
//
//    static final String name = EntityClientImpl.class.getName();
//
//    @Test
//    public void testWithMultipleInterfaces() throws ExecutionException, InterruptedException {
//        assertEquals("serverName/one.xi", client.primitive(ITestMultiple.class, "http://localhost:9000/person/id1", e -> e.name() + "/" + e.address().toString().substring(0, 6)).get());
//        assertEquals("serverName/one.xi", client.get(ITestMultiple.class, "id1", e -> e.name() + "/" + e.address().toString().substring(0, 6)).get());
//    }
//    @Test
//    public void testWithMultipleInterfaces2() throws ExecutionException, InterruptedException {
////    Thread.sleep(100000);
//        assertEquals("serverName/one.xi", client.primitive(ITestMultiple.class, "http://localhost:9000/person/id1", e -> e.name() + "/" + e.address().toString().substring(0, 6)).get());
//        //solution to this is to have a @XingYiMulti annotation and apply instance which can delegate. Actually pretty straightforwards...
//    }
//
//    @Test
//    public void testCanGetDependent() throws ExecutionException, InterruptedException {
//        assertEquals("someLine1", client.get(IPersonAddress.class, "id1", p -> p.address().line1()).get());
//        assertEquals("someLine1", client.get(ITestMultiple.class, "id1", p -> p.address().line1()).get());
//    }
}
