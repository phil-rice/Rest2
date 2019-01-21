package one.xingyi.core.httpClient;
import one.xingyi.core.endpoints.BookmarkAndUrlPattern;
import one.xingyi.core.endpoints.EndPoint;
import one.xingyi.core.endpoints.EndpointContext;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.http.ServiceResponse;
import one.xingyi.core.httpClient.domain.Entity;
import one.xingyi.core.javascript.JavascriptStore;
import one.xingyi.core.marshelling.DataAndJavaScript;
import one.xingyi.core.marshelling.IXingYiResponseSplitter;
import one.xingyi.core.marshelling.JsonObject;
import one.xingyi.core.marshelling.JsonTC;
import one.xingyi.core.utils.Strings;
import org.junit.Test;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;
public class EndPointFactoryTest {

    EndPointFactory<JsonObject> factory = EndPointFactory.<JsonObject, EntityDetailsRequest, Entity>bookmarked("/<id>", EntityDetailsRequest::create,
            req -> CompletableFuture.completedFuture(new Entity("faked" + req.entityName)));
    EndPointFactory<JsonObject> optionalFactory = EndPointFactory.<JsonObject, EntityDetailsRequest, Entity>optionalBookmarked("/<id>", EntityDetailsRequest::create,
            req -> CompletableFuture.completedFuture(Optional.of(new Entity("faked" + req.entityName))));
    EndpointContext<JsonObject> context = new EndpointContext<>(JavascriptStore.constant("someJavascript"), JsonTC.cheapJson, "http://");

    @Test
    public void testBookmarked() throws ExecutionException, InterruptedException {
        EndPoint endPoint = factory.apply(context);
        ServiceResponse serviceResponse = endPoint.apply(new ServiceRequest("get", "/person", List.of(), "")).get().get();
        assertEquals(200, serviceResponse.statusCode);
        DataAndJavaScript dataAndJavaScript = IXingYiResponseSplitter.splitter.apply(serviceResponse);
        assertEquals(Strings.changeQuotes("{'urlPattern':'faked/person'}"), dataAndJavaScript.data);
        assertEquals("someJavascript", dataAndJavaScript.javascript);
    }
    @Test
    public void testOptionalBookmarked() throws ExecutionException, InterruptedException {
        EndPoint endPoint = optionalFactory.apply(context);
        ServiceResponse serviceResponse = endPoint.apply(new ServiceRequest("get", "/person", List.of(), "")).get().get();
        assertEquals(200, serviceResponse.statusCode);
        DataAndJavaScript dataAndJavaScript = IXingYiResponseSplitter.splitter.apply(serviceResponse);
        assertEquals(Strings.changeQuotes("{'urlPattern':'faked/person'}"), dataAndJavaScript.data);
        assertEquals("someJavascript", dataAndJavaScript.javascript);
    }

}