package one.xingyi.reference;
//

import one.xingyi.core.access.IEntityStore;
import one.xingyi.core.endpoints.EndPoint;
import one.xingyi.core.endpoints.EndpointAcceptor1;
import one.xingyi.core.endpoints.EndpointContext;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.http.ServiceResponse;
import one.xingyi.core.httpClient.*;
import one.xingyi.core.httpClient.client.companion.UrlPatternCompanion;
import one.xingyi.core.httpClient.client.view.UrlPattern;
import one.xingyi.core.httpClient.server.companion.EntityCompanion;
import one.xingyi.core.javascript.JavascriptStore;
import one.xingyi.core.marshelling.*;
import one.xingyi.core.utils.Files;
import one.xingyi.reference.address.client.view.AddressLine12View;
import one.xingyi.reference.address.domain.Address;
import one.xingyi.reference.address.server.companion.AddressCompanion;
import one.xingyi.reference.person.client.view.PersonNameView;
import one.xingyi.reference.person.domain.Person;
import one.xingyi.reference.person.server.companion.PersonCompanion;
import one.xingyi.reference.telephone.domain.TelephoneNumber;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
abstract class AbstractEntityClientTest {
    static String protocolHostAndPort = "http://localhost:9000";

    abstract protected Function<ServiceRequest, CompletableFuture<ServiceResponse>> httpClient();
    abstract protected String expectedHost();

    static TelephoneNumber number = new TelephoneNumber("someNumber");
    static Address address = new Address("someLine1", "someLine2", "somePostCode");
    static Person person = new Person("serverName", 23, address, number);
    static IEntityStore<Person> personStore = IEntityStore.map(Map.of("id1", person));
    static IEntityStore<Address> addressStore = IEntityStore.map(Map.of("add1", address));

    static JsonTC<JsonObject> jsonTC = JsonTC.cheapJson;
    static String javascript = Files.getText("header.js") + "\n" + EntityCompanion.companion.javascript + "\n" + PersonCompanion.companion.javascript;

    static JavascriptStore javascriptStore = JavascriptStore.constant(javascript);
    static EndpointContext<JsonObject> endpointContext = new EndpointContext<>(javascriptStore, jsonTC, "http://");

    static EntityRegister entityRegister = EntityRegister.apply(EntityCompanion.companion, PersonCompanion.companion, AddressCompanion.companion);
    static EndPointFactory<JsonObject> entityFactory = EndPointFactory.optionalBookmarked("/<id>", EntityDetailsRequest::create, entityRegister);
    static EndPoint entityEndpoint = entityFactory.apply(endpointContext);

    static EndPointFactory<JsonObject> personEndpointFactory = EndPointFactory.optionalBookmarked(PersonCompanion.companion.bookmarkAndUrl().urlPattern, (sr, s) -> s, personStore::read);
    static EndPoint personEndpoint = personEndpointFactory.apply(endpointContext);

    EndPoint entityEndpoints() {
        if (entityFactory == null) throw new NullPointerException();
        if (entityEndpoint == null) throw new NullPointerException();
        return EndPoint.compose(entityEndpoint, personEndpoint);
    }
    ;

    HttpService rawService;
    HttpService service() {
        if (rawService == null) rawService = HttpService.defaultService(protocolHostAndPort, httpClient());
        return rawService;
    }
    static ServiceRequest sr(String url) {
        return new ServiceRequest("get", protocolHostAndPort + url, List.of(), "");
    }

    @Test
    public void testDummyToBeDeleted() throws ExecutionException, InterruptedException {
        EndPointFactory<JsonObject> factory = EndPointFactory.optionalBookmarked("/person/<id>", (sr, s) -> s, personStore::read);
        EndPoint endPoint = factory.apply(endpointContext);
        ServiceResponse serviceResponse = endPoint.apply(sr("/person/id1")).get().get();
        assertEquals(serviceResponse.toString(), 200, serviceResponse.statusCode);
        DataAndJavaScript dataAndJavaScript = IXingYiResponseSplitter.splitter.apply(serviceResponse);
        assertEquals(javascript, dataAndJavaScript.javascript);
        assertEquals(person.toJsonString(jsonTC, ContextForJson.nullContext), dataAndJavaScript.data);

    }
    @Test
    public void testDummy2ToBeDeleted() throws ExecutionException, InterruptedException {
        EndPointFactory<JsonObject> factory = EndPointFactory.optionalBookmarked("/<id>", (sr, s) -> s, personStore::read);
        EndPoint endPoint = factory.apply(endpointContext);
        ServiceResponse serviceResponse = endPoint.apply(sr("/id1")).get().get();
        assertEquals(serviceResponse.toString(), 200, serviceResponse.statusCode);
        DataAndJavaScript dataAndJavaScript = IXingYiResponseSplitter.splitter.apply(serviceResponse);
        assertEquals(javascript, dataAndJavaScript.javascript);
        assertEquals(person.toJsonString(jsonTC, ContextForJson.nullContext), dataAndJavaScript.data);

    }

    @Test
    public void testGetPrimitive() throws ExecutionException, InterruptedException {
        assertEquals(expectedHost() + "/person/<id>", service().primitiveGet(UrlPatternCompanion.companion, "/person", UrlPattern::urlPattern).get());
        assertEquals(expectedHost() + "/person/<id>", UrlPattern.getPrimitive(service(), "/person", e -> e.urlPattern()).get());
    }


    @Test
    public void testGetUrlPattern() throws ExecutionException, InterruptedException {
        assertEquals(expectedHost() + "/person/<id>", PersonNameView.getUrlPattern(service()).get());
        assertEquals(expectedHost() + "/address/<id>", AddressLine12View.getUrlPattern(service()).get());
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
        assertEquals("serverName", PersonNameView.get(service(), "id1", PersonNameView::name).get());
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
//        //solution to this is to have a @XingYiMulti annotation and create instance which can delegate. Actually pretty straightforwards...
//    }
//
//    @Test
//    public void testCanGetDependent() throws ExecutionException, InterruptedException {
//        assertEquals("someLine1", client.get(IPersonAddress.class, "id1", p -> p.address().line1()).get());
//        assertEquals("someLine1", client.get(ITestMultiple.class, "id1", p -> p.address().line1()).get());
//    }
}
