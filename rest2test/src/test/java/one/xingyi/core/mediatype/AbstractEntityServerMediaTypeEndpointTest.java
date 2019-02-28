package one.xingyi.core.mediatype;
import one.xingyi.core.endpoints.EndPoint;
import one.xingyi.core.endpoints.EndpointConfig;
import one.xingyi.core.endpoints.EndpointContext;
import one.xingyi.core.endpoints.IResourceEndpointAcceptor;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.http.ServiceResponse;
import one.xingyi.core.marshelling.ContextForJson;
import one.xingyi.core.marshelling.DataToBeSentToClient;
import one.xingyi.core.utils.FunctionFixture;
import one.xingyi.json.Json;
import one.xingyi.reference3.person.server.companion.PersonCompanion;
import one.xingyi.reference3.person.server.domain.Person;
import one.xingyi.test.IReferenceFixture3;
import org.junit.Test;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
public abstract class AbstractEntityServerMediaTypeEndpointTest<M extends IXingYiServerMediaTypeDefn<Person>> implements FunctionFixture, IReferenceFixture3 {

    protected abstract M serverMediaType();
    private EndpointContext<Object> context = EndpointConfig.defaultConfig(new Json()).from(List.of(PersonCompanion.companion));
//    private IResourceEndpointAcceptor<String> acceptor = IResourceEndpointAcceptor.apply("get", "/person/{id}", (sr, s) -> sr.path + ":" + s);

//    protected EndPoint endPoint(Function<String, CompletableFuture<Person>> fn) {return serverMediaType().<Object, String>entityEndpoint(context, acceptor, 202, fn, p -> "");}

    protected IResourceEndpoints<Person> endpoints = serverMediaType().endpoints("http", PersonCompanion.companion.bookmarkAndUrl(), p -> "");

    protected ServiceRequest sr(String url) {return ServiceRequest.sr("get", url, "application/xingyi.json.javascript.person");}

    @Test public void testGetReturnsNoneifDoesntMatch() throws ExecutionException, InterruptedException {
        Optional<ServiceResponse> opt = endpoints.get(kleisli("never called", person)).apply(sr("/doesnt/match")).get();
        assertEquals(Optional.empty(), opt);
    }
    @Test public void testGetReturnsDataAndDefnifMatchs() throws ExecutionException, InterruptedException {
        ServiceRequest serviceRequest = sr("/person/someId");
        DataToBeSentToClient expected = serverMediaType().makeDataAndDefn(ContextForJson.forServiceRequest("http", serviceRequest), p -> "", person);
        ServiceResponse resp = endpoints.get(kleisli("someId", person)).apply(serviceRequest).get().get();
        assertEquals(200, resp.statusCode);
        assertEquals(expected.asString(), resp.body);
    }

    @Test public void testTheJsonContainsLinks() {
        DataToBeSentToClient dataToBeSent = serverMediaType().makeDataAndDefn(ContextForJson.forServiceRequest("http", sr("/person/someId")), p -> "", person);
        String data = dataToBeSent.data;
        assertTrue(data, data.contains("_links"));
        assertTrue(data, data.contains("/person/someId"));

    }

}