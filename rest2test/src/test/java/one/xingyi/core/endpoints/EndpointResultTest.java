package one.xingyi.core.endpoints;
import one.xingyi.core.http.Header;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.http.ServiceResponse;
import one.xingyi.core.javascript.JavascriptDetailsToString;
import one.xingyi.core.marshelling.*;
import one.xingyi.core.sdk.TestResource;
import one.xingyi.core.utils.FunctionFixture;
import one.xingyi.core.utils.IdAndValue;
import one.xingyi.json.Json;
import org.junit.Test;

import java.util.List;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
public class EndpointResultTest implements FunctionFixture {

    ServiceRequest serviceRequestNoHost = new ServiceRequest("someMethod", "/somePath", List.of(), "");
    ServiceRequest serviceRequestHost = new ServiceRequest("someMethod", "/somePath", List.of(new Header("host", "someHost")), "");
    EndpointContext<Object> context = new EndpointConfig<>("rootJavascript", new Json(), "http://", JavascriptDetailsToString.simple, IMergeJavascriptAndJson.simple).from(List.of());


    Function<TestResource, String> stateFn = e -> "someState";

    @Test public void testCreate() {
        ServiceResponse serviceResponse = EndpointResult.create(context, "/some/{id}", 314).apply(serviceRequestHost, new TestResource());
        assertEquals(314, serviceResponse.statusCode);
        assertEquals("rootJavascript\n" +
                "---------\n" +
                "{\"test\":\"parserAndWriter\"}", serviceResponse.body);
    }
    @Test public void testCreateForIdAndValue() {
        ServiceResponse serviceResponse = EndpointResult.<Object, TestResource>createForIdAndvalue(context, "/some/{id}", 314, e -> "").apply(serviceRequestHost, new IdAndValue<TestResource>("someId", new TestResource()));
        assertEquals(314, serviceResponse.statusCode);
        assertEquals("rootJavascript\n" +
                "---------\n" +
                "{\"id\":\"someId\",\"value\":{\"test\":\"parserAndWriter\"}}", serviceResponse.body);
    }
    @Test public void testCreateForNonEntity() {
        TestResource entity = new TestResource();
        ServiceResponse serviceResponse = EndpointResult.createForNonEntity(314, fn(entity, "someJson")).apply(serviceRequestHost, entity);
        assertEquals(314, serviceResponse.statusCode);
        assertEquals("someJson", serviceResponse.body);
    }

}