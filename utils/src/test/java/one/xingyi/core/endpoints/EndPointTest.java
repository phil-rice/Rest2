package one.xingyi.core.endpoints;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.http.ServiceResponse;
import one.xingyi.core.javascript.JavascriptDetailsToString;
import one.xingyi.core.javascript.JavascriptStore;
import one.xingyi.core.marshelling.JsonObject;
import one.xingyi.core.marshelling.JsonParser;
import one.xingyi.core.marshelling.JsonWriter;
import one.xingyi.core.sdk.TestEntity;
import one.xingyi.core.utils.FunctionFixture;
import one.xingyi.core.utils.Strings;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;
public class EndPointTest implements FunctionFixture {

    EndpointContext<JsonObject> context = new EndpointContext<JsonObject>(JavascriptStore.constant("someJavascript"), JavascriptDetailsToString.simple, JsonWriter.cheapJson, JsonParser.nullParser(),
            "http://");
    TestEntity testEntity = new TestEntity();

    void checkSR(int statusCode, String json, ServiceResponse serviceResponse) {
        assertEquals(statusCode, serviceResponse.statusCode);
        assertEquals("someJavascript\n---------\n" + json, serviceResponse.body);
    }

    @Test
    public void getOptionalEntity() {
    }
    @Test
    public void getEntity() throws ExecutionException, InterruptedException {
        checkSR(200, Strings.changeQuotes("{'test':'json'}"), EndPoint.getEntity(context, "/path/{id}", kleisli("someId", testEntity)).apply(ServiceRequest.sr("get", "/path/someId")).get().get());
        checkSR(200, Strings.changeQuotes("{'test':'json'}"), EndPoint.getEntity(context, "{host}/path/{id}", kleisli("someId", testEntity)).apply(ServiceRequest.sr("get", "/path/someId")).get().get());
    }
    @Test
    public void deleteEntity() {
    }
    @Test
    public void createEntity() {
    }
    @Test
    public void createEntityWithId() {
    }
    @Test
    public void postEntity() {
    }
    @Test
    public void toKliesli() {
    }
    @Test
    public void internalError() {
    }
    @Test
    public void json() {
    }
    @Test
    public void javascriptAndJson() {
    }
    @Test
    public void optionalJavascriptAndJson() {
    }
    @Test
    public void compose() {
    }
    @Test
    public void staticEndpoint() {
    }
}