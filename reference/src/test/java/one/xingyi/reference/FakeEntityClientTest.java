package one.xingyi.reference;
import one.xingyi.core.endpoints.EndPoint;
import one.xingyi.core.http.JavaHttpClient;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.http.ServiceResponse;
import one.xingyi.core.httpClient.HttpService;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
public class FakeEntityClientTest extends AbstractEntityClientTest {

    @Override protected Function<ServiceRequest, CompletableFuture<ServiceResponse>> httpClient() { return EndPoint.toKliesli(entityEndpoint); }
    @Override protected String expectedHost() { return ""; }
}
