package one.xingyi.reference3;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import one.xingyi.core.client.ISimpleList;
import one.xingyi.core.client.MirroredSimpleList;
import one.xingyi.core.endpoints.EndPoint;
import one.xingyi.core.endpoints.EndpointConfig;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.http.ServiceResponse;
import one.xingyi.core.httpClient.HttpService;
import one.xingyi.core.marshelling.JsonValue;
import one.xingyi.reference4.PersonServer;
import one.xingyi.reference4.address.client.view.AddressLine12View;
import one.xingyi.reference4.person.PersonController;
import one.xingyi.reference4.person.client.view.PersonAddress12View;
import one.xingyi.reference4.person.client.view.PersonAddresses12View;
import one.xingyi.reference4.person.client.view.PersonLine12View;
import one.xingyi.reference4.person.server.companion.PersonCompanion;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
abstract public class AbstractDeprecated3Tests<J> {

    Function<ServiceRequest, CompletableFuture<ServiceResponse>> httpClient() { return EndPoint.toKliesli(entityEndpoints); }
    abstract EndpointConfig<JsonValue> config();
    abstract boolean supportsReadingJson();
    EndPoint entityEndpoints = EndPoint.compose(new PersonServer<JsonValue>(config(), new PersonController()).allEndpoints());
    HttpService rawService;
    HttpService service() { if (rawService == null) rawService = HttpService.defaultService("http://localhost:9000", httpClient()); return rawService; }

    @Test public void testJavascriptForLine1() {
        assertFalse(PersonCompanion.companion.javascript, PersonCompanion.companion.javascript().contains("return lens('line1')"));
        assertTrue(PersonCompanion.companion.javascript, PersonCompanion.companion.javascript().contains("return compose(lens_Person_address(), lens('line2'));"));
    }

    @Test public void testCanReadLine1and2() throws ExecutionException, InterruptedException {
        assertEquals("someLine1", PersonLine12View.get(service(), "id1", PersonLine12View::line1).get());
        assertEquals("someLine2", PersonLine12View.get(service(), "id1", PersonLine12View::line2).get());

    }
    @Test public void testCanReadLine1and2ViaAddress() throws ExecutionException, InterruptedException {
        assertEquals("someLine1", PersonAddress12View.get(service(), "id1", v -> v.address().line1()).get());
        assertEquals("someLine2", PersonAddress12View.get(service(), "id1", v -> v.address().line2()).get());

    }

    @Test public void testCanReadLine1and2ViaAddresses() throws ExecutionException, InterruptedException {
        assertEquals("someLine1", PersonAddresses12View.get(service(), "id1", v -> v.addresses().get(0).line1()).get());
        assertEquals("someLine2", PersonAddresses12View.get(service(), "id1", v -> v.addresses().get(0).line2()).get());

    }
    @Test public void testCanSizeOfReadLine1and2ViaAddresses() throws ExecutionException, InterruptedException {
        assertEquals(1, PersonAddresses12View.get(service(), "id1", v -> v.addresses().size()).get().intValue());
        assertEquals(1, PersonAddresses12View.get(service(), "id1", v -> v.addresses().size()).get().intValue());

    }
    @Test public void testCanChangeItemsInList() throws ExecutionException, InterruptedException {
        if (supportsReadingJson()) {
            assertEquals("newLine1", PersonAddresses12View.get(service(), "id1", v -> {
                AddressLine12View newItem = v.addresses().get(0).withline1("newLine1");
                ISimpleList<AddressLine12View> addresses = v.addresses();
                MirroredSimpleList<AddressLine12View> newList = (MirroredSimpleList<AddressLine12View>) addresses.withItem(0, newItem);
                ScriptObjectMirror newListAsJava= (ScriptObjectMirror) newList.mirror;
                return newList.get(0).line1();
            }).get());
            assertEquals("newLine2", PersonAddresses12View.get(service(), "id1", v -> v.addresses().withItem(0, v.addresses().get(0).withline2("newLine2")).get(0).line2()).get());
        }
    }

}
