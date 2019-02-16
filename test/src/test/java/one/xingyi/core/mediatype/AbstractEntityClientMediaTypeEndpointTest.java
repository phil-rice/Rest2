package one.xingyi.core.mediatype;

import one.xingyi.core.endpoints.EndPoint;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.http.ServiceResponse;
import one.xingyi.core.marshelling.ContextForJson;
import one.xingyi.core.marshelling.DataToBeSentToClient;
import one.xingyi.reference3.person.client.entitydefn.IPersonNameViewClientEntity;
import one.xingyi.reference3.person.client.view.PersonNameView;
import one.xingyi.reference3.person.server.domain.Person;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

import static junit.framework.TestCase.assertEquals;

public abstract class AbstractEntityClientMediaTypeEndpointTest<ServerMediaType extends IXingYiServerMediaTypeDefn<Person>, ClientMediaType extends SimpleClientMediaTypeDefn<IPersonNameViewClientEntity, PersonNameView>> extends AbstractEntityServerMediaTypeEndpointTest {
    abstract ClientMediaType clientMediaType();

    @Test public void testCanTurnAResponseIntoAPersonView() throws ExecutionException, InterruptedException {
        EndPoint endPoint = endpoints.get(kleisli("someId", person));
        ServiceResponse resp = endPoint.apply(sr("/person/someId")).get().get();
        assertEquals(200, resp.statusCode); //just in case

        PersonNameView personNameView = clientMediaType().makeFrom(resp).get();
        assertEquals("someName", personNameView.name());
    }


    @Test public void testGetEndpoint(){

    }
}
