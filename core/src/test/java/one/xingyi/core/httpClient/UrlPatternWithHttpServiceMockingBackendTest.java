package one.xingyi.core.httpClient;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.http.ServiceResponse;
import one.xingyi.core.httpClient.client.companion.UrlPatternCompanion;
import one.xingyi.core.httpClient.client.view.UrlPattern;
import one.xingyi.core.httpClient.server.companion.EntityDetailsCompanion;
import one.xingyi.core.httpClient.server.domain.EntityDetails;
import one.xingyi.core.marshelling.ContextForJson;
import one.xingyi.core.marshelling.IXingYiResponseSplitter;
import one.xingyi.core.marshelling.JsonObject;
import one.xingyi.core.marshelling.JsonWriter;
import one.xingyi.core.utils.Files;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
public class UrlPatternWithHttpServiceMockingBackendTest {
    static Function<UrlPattern, String> getFn = UrlPattern::urlPattern;
    static String bookmark = "/bookmarkAndUrl";
    static String protocolAndHost = "http://someHost:9000";
    static String url = protocolAndHost + bookmark;
    static ServiceRequest serviceRequest = new ServiceRequest("get", url, List.of(), "");
    static ContextForJson context = ContextForJson.forServiceRequest("http://", serviceRequest);
    static JsonWriter<JsonObject> jsonTC = JsonWriter.cheapJson;
    static String javascript = Files.getText("header.js") + EntityDetailsCompanion.companion.javascript;
    static String json = new EntityDetails("http://someHost:9000/someUrlPattern{id}").toJsonString(jsonTC, context);

    static String responseBody = javascript + IXingYiResponseSplitter.marker + json;
    static ServiceResponse serviceResponse = new ServiceResponse(200, responseBody, List.of());

    @Test
    public void testCanGet_checkingTheActualServiceRequestAndResponse() throws ExecutionException, InterruptedException {
        Function<ServiceRequest, CompletableFuture<ServiceResponse>> delegate = mock(Function.class);
        HttpService service = HttpService.defaultService(protocolAndHost, delegate);
        when(delegate.apply(serviceRequest)).thenReturn(CompletableFuture.completedFuture(serviceResponse));

        assertEquals("http://someHost:9000/someUrlPattern{id}", service.primitiveGet(UrlPatternCompanion.companion, bookmark, getFn).get());
    }
}

