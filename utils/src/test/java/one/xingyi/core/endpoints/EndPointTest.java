package one.xingyi.core.endpoints;
import one.xingyi.core.http.ServiceResponse;
import one.xingyi.core.javascript.JavascriptDetailsToString;
import one.xingyi.core.javascript.JavascriptStore;
import one.xingyi.core.marshelling.JsonValue;
import one.xingyi.core.marshelling.JsonParser;
import one.xingyi.core.marshelling.JsonWriter;
import one.xingyi.core.sdk.TestResource;
import one.xingyi.core.utils.FunctionFixture;
import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.function.Function;

import static org.junit.Assert.*;
public class EndPointTest implements FunctionFixture {

    EndpointContext<JsonValue> context = new EndpointContext<JsonValue>(
            JavascriptStore.constant("someJavascript"), JavascriptDetailsToString.simple, IMergeJavascriptAndJson.simple,
            JsonWriter.cheapJson,
            "http://");
    TestResource testEntity = new TestResource();

    void checkSR(int statusCode, String json, ServiceResponse serviceResponse) {
        assertEquals(statusCode, serviceResponse.statusCode);
        assertEquals("someJavascript\n---------\n" + json, serviceResponse.body);
    }

    Function<TestResource, String> stateFn = e -> "someState";

    @Test
    public void getOptionalEntity() {
    }
    @Test
    public void getEntity() throws ExecutionException, InterruptedException {
//        checkSR(200, Strings.changeQuotes("{'some':'parserAndWriter'}"), EndPoint.getEntity(context, "/templatedPath/{id}", kleisli("someId", testEntity),stateFn).apply(ServiceRequest.sr("get", "/templatedPath/someId")).get().get());
//        checkSR(200, Strings.changeQuotes("{'some':'parserAndWriter'}"), EndPoint.getEntity(context, "{host}/templatedPath/{id}", kleisli("someId", testEntity),stateFn).apply(ServiceRequest.sr("get", "/templatedPath/someId")).get().get());
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