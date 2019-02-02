package one.xingyi.reference4;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import one.xingyi.core.client.IResourceList;
import one.xingyi.core.client.MirroredResourceList;
import one.xingyi.core.endpoints.EndPoint;
import one.xingyi.core.endpoints.EndpointConfig;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.http.ServiceResponse;
import one.xingyi.core.httpClient.HttpServiceCompletableFuture;
import one.xingyi.core.marshelling.IXingYiResponseSplitter;
import one.xingyi.core.marshelling.JsonValue;
import one.xingyi.reference4.address.client.view.AddressLine12View;
import one.xingyi.reference4.person.PersonController;
import one.xingyi.reference4.person.client.view.PersonAddress12View;
import one.xingyi.reference4.person.client.view.PersonAddresses12View;
import one.xingyi.reference4.person.client.view.PersonLine12View;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

import static junit.framework.TestCase.assertEquals;
abstract public class AbstractDeprecated4Tests<J> {

    Function<ServiceRequest, CompletableFuture<ServiceResponse>> httpClient() { return EndPoint.toKliesli(entityEndpoints); }
    abstract EndpointConfig<JsonValue> config();
    abstract boolean supportsReadingJson();
    EndPoint entityEndpoints = EndPoint.compose(new PersonServer<JsonValue>(config(), new PersonController()).allEndpoints());
    HttpServiceCompletableFuture rawService;
    HttpServiceCompletableFuture service() {
        if (rawService == null) {
            Function<ServiceRequest, CompletableFuture<ServiceResponse>> client = httpClient();
            rawService = HttpServiceCompletableFuture.defaultService("http://localhost:9000", client);
        }
        return rawService;
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
                IResourceList<AddressLine12View> addresses = v.addresses();
                MirroredResourceList<AddressLine12View> newList = (MirroredResourceList<AddressLine12View>) addresses.withItem(0, newItem);
                ScriptObjectMirror newListAsJava = (ScriptObjectMirror) newList.mirror;
                return newList.get(0).line1();
            }).get());
            assertEquals("newLine2", PersonAddresses12View.get(service(), "id1", v -> v.addresses().withItem(0, v.addresses().get(0).withline2("newLine2")).get(0).line2()).get());
        }
    }

}
