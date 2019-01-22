package one.xingyi.test;
import one.xingyi.core.endpoints.EndPoint;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.http.ServiceResponse;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
public class FakeEntityDetailsClientTest extends AbstractEntityDetailsClientTest {

    @Override protected Function<ServiceRequest, CompletableFuture<ServiceResponse>> httpClient() { return EndPoint.toKliesli(entityEndpoints); }
    @Override protected String expectedHost() { return ""; }
}
