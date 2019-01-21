package one.xingyi.reference;
//

import one.xingyi.core.access.IEntityStore;
import one.xingyi.core.endpoints.EndPoint;
import one.xingyi.core.endpoints.EndpointAcceptor1;
import one.xingyi.core.endpoints.EndpointContext;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.http.ServiceResponse;
import one.xingyi.core.httpClient.*;
import one.xingyi.core.httpClient.client.view.UrlPattern;
import one.xingyi.core.httpClient.server.companion.EntityCompanion;
import one.xingyi.core.javascript.JavascriptStore;
import one.xingyi.core.marshelling.*;
import one.xingyi.core.utils.Files;
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
abstract class AbstractEntityClientTest {
    String protocolHostAndPort = "http://localhost:9000";

    abstract protected Function<ServiceRequest, CompletableFuture<ServiceResponse>> httpClient();
    abstract protected String expectedHost();

    TelephoneNumber number = new TelephoneNumber("someNumber");
    Address address = new Address("someLine1", "someLine2", "somePostCode");
    Person person = new Person("serverName", 23, address, number);
    IEntityStore<Person> personStore = IEntityStore.map(Map.of("id1", person));
    IEntityStore<Address> addressStore = IEntityStore.map(Map.of("add1", address));

    JsonTC<JsonObject> jsonTC = JsonTC.cheapJson;
    static String javascript = Files.getText("header.js") + EntityCompanion.companion.javascript;

    JavascriptStore javascriptStore = JavascriptStore.constant(javascript);
    EndpointContext<JsonObject> endpointContext = new EndpointContext<>(javascriptStore, jsonTC);
    HttpService service = HttpService.defaultService(protocolHostAndPort, httpClient());

    ServiceRequest sr(String url) {
        return new ServiceRequest("get", protocolHostAndPort + url, List.of(), "");

    }
    @Test
    public void testCompanion() throws ExecutionException, InterruptedException {
        assertEquals(200, PersonCompanion.companion.entityEndpoint().apply(sr(protocolHostAndPort + "/person")).get().get().statusCode);
    }

    @Test
    public void testDummyToBeDeleted() throws ExecutionException, InterruptedException {
        EndPointFactory<JsonObject> factory = EndPointFactory.optionalBookmarked("/person/<id>", Function.identity(), personStore::read);
        EndPoint endPoint = factory.apply(endpointContext);
        ServiceResponse serviceResponse = endPoint.apply(sr("/person/id1")).get().get();
        assertEquals(serviceResponse.toString(), 200, serviceResponse.statusCode);
        DataAndJavaScript dataAndJavaScript = IXingYiResponseSplitter.splitter.apply(serviceResponse);
        assertEquals(javascript, dataAndJavaScript.javascript);
        assertEquals(person.toJsonString(jsonTC, ContextForJson.nullContext), dataAndJavaScript.data);

    }

    @Test
    public void testGetPrimitive() throws ExecutionException, InterruptedException {
        assertEquals(expectedHost() + "/person/<id>", UrlPattern.getPrimitive(service, "/person", e -> e.urlPattern()).get());
    }


    @Test
    public void testGetUrlPattern() throws ExecutionException, InterruptedException {
//        CompletableFuture<String> entity = UrlPattern.getUrlPattern(service);
//        assertEquals(expectedHost() + "/entity/<id>", entity.get());
        assertEquals(expectedHost() + "/person/<id>", PersonNameView.getUrlPattern(service).get());
        assertEquals(expectedHost() + "/address/<id>", UrlPattern.get(service, "address", UrlPattern::urlPattern).get());
    }
    @Test
    public void testGetUrlPatternWhenEntityNotRegistered() throws ExecutionException, InterruptedException {
        UrlPattern.get(service, "someUnknownEntity", UrlPattern::urlPattern).get();
    }
//
//    @Test
//    public void testGetPerson() throws ExecutionException, InterruptedException {
//        assertEquals("serverName", client.get(IPersonName.class, "id1", IPersonName::name).get());
//    }
//
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
