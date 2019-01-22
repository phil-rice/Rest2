package one.xingyi.test;
import one.xingyi.core.endpoints.EndPoint;
import one.xingyi.core.endpoints.EndPointFactory;
import one.xingyi.core.endpoints.EndpointContext;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.http.ServiceResponse;
import one.xingyi.core.httpClient.EntityDetailsRequest;
import one.xingyi.core.httpClient.domain.EntityDetails;
import one.xingyi.core.javascript.JavascriptStore;
import one.xingyi.core.marshelling.DataAndJavaScript;
import one.xingyi.core.marshelling.IXingYiResponseSplitter;
import one.xingyi.core.marshelling.JsonObject;
import one.xingyi.core.marshelling.JsonTC;
import one.xingyi.core.utils.Strings;
import one.xingyi.server.EndPointFactorys;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
public class EndPointFactoryTest {

    EndPointFactory factory = EndPointFactorys.<EntityDetailsRequest, EntityDetails>bookmarked("/<id>", EntityDetailsRequest::create,
            req -> CompletableFuture.completedFuture(new EntityDetails("faked" + req.entityName)));
    EndPointFactory optionalFactory = EndPointFactorys.<EntityDetailsRequest, EntityDetails>optionalBookmarked("/<id>", EntityDetailsRequest::create,
            req -> CompletableFuture.completedFuture(Optional.of(new EntityDetails("faked" + req.entityName))));
    EndpointContext<JsonObject> context = new EndpointContext<>(JavascriptStore.constant("someJavascript"), JsonTC.cheapJson, "http://");

    @Test
    public void testBookmarked() throws ExecutionException, InterruptedException {
        EndPoint endPoint = factory.create(context);
        ServiceResponse serviceResponse = endPoint.apply(new ServiceRequest("get", "/person", List.of(), "")).get().get();
        Assert.assertEquals(200, serviceResponse.statusCode);
        DataAndJavaScript dataAndJavaScript = IXingYiResponseSplitter.splitter.apply(serviceResponse);
        Assert.assertEquals(Strings.changeQuotes("{'urlPattern':'faked/person'}"), dataAndJavaScript.data);
        Assert.assertEquals("someJavascript", dataAndJavaScript.javascript);
    }
    @Test
    public void testOptionalBookmarked() throws ExecutionException, InterruptedException {
        EndPoint endPoint = optionalFactory.create(context);
        ServiceResponse serviceResponse = endPoint.apply(new ServiceRequest("get", "/person", List.of(), "")).get().get();
        Assert.assertEquals(200, serviceResponse.statusCode);
        DataAndJavaScript dataAndJavaScript = IXingYiResponseSplitter.splitter.apply(serviceResponse);
        Assert.assertEquals(Strings.changeQuotes("{'urlPattern':'faked/person'}"), dataAndJavaScript.data);
        Assert.assertEquals("someJavascript", dataAndJavaScript.javascript);
    }

}