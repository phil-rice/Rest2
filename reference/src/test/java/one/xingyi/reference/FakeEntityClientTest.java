package one.xingyi.reference;
import one.xingyi.core.endpoints.EndPoint;
import one.xingyi.core.http.JavaHttpClient;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.http.ServiceResponse;
import one.xingyi.core.httpClient.*;
import one.xingyi.core.httpClient.server.companion.EntityCompanion;
import one.xingyi.core.marshelling.JsonObject;
import one.xingyi.reference.address.server.companion.AddressCompanion;
import one.xingyi.reference.person.server.companion.PersonCompanion;
import org.mockito.internal.matchers.Null;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
public class FakeEntityClientTest extends AbstractEntityClientTest {

    @Override protected Function<ServiceRequest, CompletableFuture<ServiceResponse>> httpClient() { return EndPoint.toKliesli(entityEndpoints()); }
    @Override protected String expectedHost() { return ""; }
}
