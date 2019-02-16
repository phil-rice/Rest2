package one.xingyi.core.mediatype;
import one.xingyi.core.endpoints.EndpointConfig;
import one.xingyi.core.endpoints.EndpointContext;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.http.ServiceResponse;
import one.xingyi.json.Json;
import one.xingyi.reference3.person.client.companion.PersonNameViewCompanion;
import one.xingyi.reference3.person.client.view.PersonNameView;
import one.xingyi.reference3.person.server.companion.PersonCompanion;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static junit.framework.TestCase.assertEquals;
public class MediaTypeInCompanionsTests {

    PersonCompanion serverCompanion = PersonCompanion.companion;
    PersonNameViewCompanion viewCompanion = PersonNameViewCompanion.companion;
    EndpointConfig<Object> config = EndpointConfig.defaultConfig(new Json());
    EndpointContext<Object> context = config.from(List.of(PersonCompanion.companion));
    //    PersonServer<Object> server = new PersonServer<>(config, new PersonController());

//    Function<ServiceRequest, CompletableFuture<ServiceResponse>> endpoint = serverCompanion.lensMediaDefn(context).entityEndpoint()endallEndpoints(context, new PersonController())));

    @Test public void testCanGetViewFromServer() throws ExecutionException, InterruptedException {
//        serverCompanion.lensMediaDefn(context).entityEndpoint();
//        ServiceResponse serviceResponse = endpoint.apply(ServiceRequest.sr("get", "/person/id1")).get();
//        PersonNameView personNameView = viewCompanion.x(config.parserAndWriter).makeFrom(serviceResponse).get();
//        assertEquals("", personNameView.name());

    }
}
