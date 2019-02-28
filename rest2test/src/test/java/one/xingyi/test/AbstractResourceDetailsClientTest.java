package one.xingyi.test;
//

import one.xingyi.core.endpoints.EndPoint;
import one.xingyi.core.endpoints.EndpointConfig;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.http.ServiceResponse;
import one.xingyi.core.httpClient.HttpServiceCompletableFuture;
import one.xingyi.core.httpClient.client.view.UrlPattern;
import one.xingyi.core.httpClient.client.viewcompanion.UrlPatternCompanion;
import one.xingyi.core.marshelling.UnexpectedResponse;
import one.xingyi.core.utils.DigestAndString;
import one.xingyi.core.utils.IdAndValue;
import one.xingyi.democlient.client.compositeView.IPersonNameLine12View;
import one.xingyi.json.Json;
import one.xingyi.reference1.person.client.view.PersonLine12View;
import one.xingyi.reference3.PersonServer;
import one.xingyi.reference3.person.PersonController;
import one.xingyi.reference3.person.client.view.PersonNameView;
import one.xingyi.reference3.person.client.viewcompanion.PersonNameViewCompanion;
import org.junit.Before;
import org.junit.Test;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
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
    static EndpointConfig<Object> config = EndpointConfig.defaultConfig(json);


    static PersonController controller = new PersonController(config.parserAndWriter);
    static PersonServer<Object> server = new PersonServer<>(config, controller);
    static EndPoint entityEndpoints = EndPoint.compose(server.allEndpoints(), true);

    HttpServiceCompletableFuture rawService;
    HttpServiceCompletableFuture service() {
        if (rawService == null)
            rawService = HttpServiceCompletableFuture.lensService("http://localhost:9000", config.parserAndWriter, httpClient());
        return rawService;
    }

    @Before
    public void resetData() {
        controller.reset();
    }

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
        assertEquals(serviceResponse.toString(), 200, serviceResponse.statusCode);
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
    @Test
    public void testGetOptionalPerson() throws ExecutionException, InterruptedException {
        assertEquals(Optional.of("someName"), PersonNameView.getOptional(service(), "id1", PersonNameView::name).get());
        assertEquals(Optional.empty(), PersonNameView.getOptional(service(), "notin", PersonNameView::name).get());
    }
    @Test
    public void testCreate() throws ExecutionException, InterruptedException {
        PersonLine12View prototype = PersonLine12View.create(service(), "prototype").get();
        PersonLine12View newView = prototype.withline1("newLine1").withline2("newLine2");
        System.out.println(prototype.xingYi().render("json", newView));
        IdAndValue<PersonLine12View> idAndView = PersonLine12View.create(service(), newView).get();
        assertEquals("2", idAndView.id);
        assertEquals("newLine1newLine2", idAndView.t.line1() + idAndView.t.line2());

    }

    @Test public void testGetCompositePerson() throws ExecutionException, InterruptedException {
        assertEquals("someNamesomeLine1", IPersonNameLine12View.get(service(), "id1", i -> i.name() + i.line1()).get());
        assertEquals(Optional.of("someNamesomeLine1"), IPersonNameLine12View.getOptional(service(), "id1", i -> i.name() + i.line1()).get());
    }
    @Test public void testEditCompositePerson() throws ExecutionException, InterruptedException {
        IPersonNameLine12View.edit(service(), "id1", i -> i.withname("newName").withline1("newLine1").withline2("newLine2")).get();
        assertEquals(Optional.of("newNamenewLine1newLine2"), IPersonNameLine12View.getOptional(service(), "id1", i -> i.name() + i.line1() + i.line2()).get());
    }
    @Test public void testDeleteCompositePerson() throws ExecutionException, InterruptedException {
        IPersonNameLine12View.delete(service(), "id1").get();
        assertEquals(Optional.empty(), IPersonNameLine12View.getOptional(service(), "id1", i -> i.name() + i.line1()).get());
    }

    @Test public void testCreateWithIdWithComposite() throws ExecutionException, InterruptedException {
        IPersonNameLine12View view = IPersonNameLine12View.create(service(), "newId").get();
        assertEquals("newId", view.name());
    }
    @Test public void testCreateWithComposite() throws ExecutionException, InterruptedException {
        IPersonNameLine12View prototype = IPersonNameLine12View.create(service(), "prototype").get();
        IPersonNameLine12View newView = prototype.withname("newName").withline1("newLine1").withline2("newLine2");
        System.out.println(prototype.xingYi().render("json", newView));
        IdAndValue<IPersonNameLine12View> idAndView = IPersonNameLine12View.create(service(), newView).get();
        assertEquals("2", idAndView.id);
        assertEquals("newNamenewLine1newLine2", idAndView.t.name() + idAndView.t.line1() + idAndView.t.line2());
    }

}
