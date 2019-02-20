package one.xingyi.core.mediatype;
import one.xingyi.core.endpoints.EndpointConfig;
import one.xingyi.core.endpoints.EndpointContext;
import one.xingyi.core.http.Header;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.http.ServiceResponse;
import one.xingyi.core.marshelling.ContextForJson;
import one.xingyi.core.marshelling.DataToBeSentToClient;
import one.xingyi.json.Json;
import one.xingyi.reference3.address.server.companion.AddressCompanion;
import one.xingyi.reference3.person.client.entitydefn.IPersonClientEntity;
import one.xingyi.reference3.person.client.view.PersonNameView;
import one.xingyi.reference3.person.server.companion.PersonCompanion;
import one.xingyi.reference3.person.server.domain.Person;
import one.xingyi.reference3.telephone.server.companion.TelephoneNumberCompanion;
import one.xingyi.test.IReferenceFixture3;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;
public abstract class SimpleMediaTypeDefnTest<MediaType extends IMediaTypeServerDefn<Person>> implements IReferenceFixture3 {

    protected static String entityName = "Person";

    protected EndpointConfig<Object> config = EndpointConfig.defaultConfig(new Json());
    protected EndpointContext<Object> context = config.from(List.of(PersonCompanion.companion, AddressCompanion.companion, TelephoneNumberCompanion.companion));
    protected abstract MediaType serverMediaDefn();
    protected abstract String acceptHeader();
    protected abstract String makeJsonFromContextAndPerson();


    ServiceRequest serviceRequest = new ServiceRequest("get", "/who/cares", List.of(
            new Header("host", "somehost"),
            new Header("accept", acceptHeader())), "unimportant");
    ContextForJson contextForJson = ContextForJson.forServiceRequest("http://", serviceRequest);

    @Test public void testCanParseAndThenRetrievePersonOnServer() {
        String acceptHeader = acceptHeader();
        String json = makeJsonFromContextAndPerson();
        Person newPerson = serverMediaDefn().makeEntityFrom(acceptHeader, json);
        assertEquals(person, newPerson);
    }
}

abstract class SimpleMediaTypeDefnClientTests<J,
        Server extends SimpleServerMediaTypeDefn<J, Person>,
        Client extends SimpleClientMediaTypeDefn<IPersonClientEntity, PersonNameView>> extends SimpleMediaTypeDefnTest<Server> {

    abstract Client clientMediaDefn();
    @Override protected String acceptHeader() { return clientMediaDefn().acceptHeader(Set.of()); }

    @Test(expected = RuntimeException.class) public void testAcceptHeaderWithUnhappyPath() {
        Set<String> capabilities = Set.of("one", "two", "three");
        String acceptHeader = "wrong" + acceptHeader();
        assertFalse(acceptHeader, serverMediaDefn().accept(acceptHeader));
        serverMediaDefn().lensNames(acceptHeader);
    }

    @Test public void testAcceptHeaderWithhappyPath() {
        Set<String> capabilities = Set.of("one", "two", "three");
        String acceptHeader = clientMediaDefn().acceptHeader(capabilities);
        assertTrue(acceptHeader.contains("." + entityName + "."));
        assertTrue(acceptHeader, serverMediaDefn().accept(acceptHeader));
        assertEquals(acceptHeader, capabilities, new HashSet<>(serverMediaDefn().lensNames(acceptHeader)));
    }

    @Test public void testCanTurnAPersonOnTheServerIntoAView() throws ExecutionException, InterruptedException {
        DataToBeSentToClient dataToBeSentToClient = serverMediaDefn().makeDataAndDefn(contextForJson, p -> "", person);
        PersonNameView newPerson = clientMediaDefn().makeFrom(new ServiceResponse(200, dataToBeSentToClient.asString(), List.of())).get();
        assertEquals(person.name(), newPerson.name());
    }

    @Test public void testCanTurnAPersonAndIdOnTheServerIntoAView() throws ExecutionException, InterruptedException {
        DataToBeSentToClient dataToBeSentToClient = serverMediaDefn().makeDataAndDefn(contextForJson, p -> "", person);
        PersonNameView newPerson = clientMediaDefn().makeFrom(new ServiceResponse(200, dataToBeSentToClient.asString(), List.of())).get();
        assertEquals(person.name(), newPerson.name());
    }

}