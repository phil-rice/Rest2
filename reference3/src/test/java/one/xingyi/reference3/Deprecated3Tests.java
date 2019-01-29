package one.xingyi.reference3;
import one.xingyi.core.endpoints.EndPoint;
import one.xingyi.core.endpoints.EndpointConfig;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.http.ServiceResponse;
import one.xingyi.core.httpClient.HttpService;
import one.xingyi.core.marshelling.JsonValue;
import one.xingyi.reference3.person.PersonController;
import one.xingyi.reference3.person.client.view.PersonLine12View;
import one.xingyi.reference3.person.server.companion.PersonCompanion;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
public class Deprecated3Tests {

    Function<ServiceRequest, CompletableFuture<ServiceResponse>> httpClient() { return EndPoint.toKliesli(entityEndpoints); }
    static EndpointConfig<JsonValue> config = EndpointConfig.defaultConfigNoParser;
    static EndPoint entityEndpoints = EndPoint.compose(new PersonServer<JsonValue>(config, new PersonController()).allEndpoints());
    HttpService rawService;
    HttpService service() { if (rawService == null) rawService = HttpService.defaultService("http://localhost:9000", httpClient()); return rawService; }

    @Test public void testJavascriptForLine1() {
        assertFalse(PersonCompanion.companion.javascript, PersonCompanion.companion.javascript().contains("return lens('line1')"));
        assertTrue(PersonCompanion.companion.javascript, PersonCompanion.companion.javascript().contains("return compose(lens_Person_address(), lens('line2'));"));
    }

    @Test public void testCanReadLine1() throws ExecutionException, InterruptedException {
        assertEquals("someLine1", PersonLine12View.get(service(), "id1", PersonLine12View::line1).get());

    }

}
