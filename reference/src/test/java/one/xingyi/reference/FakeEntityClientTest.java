package one.xingyi.reference;
import one.xingyi.core.endpoints.EndPoint;
import one.xingyi.core.http.JavaHttpClient;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.http.ServiceResponse;
import one.xingyi.core.httpClient.EntityDetailsEndpoint;
import one.xingyi.core.httpClient.EntityRegister;
import one.xingyi.core.httpClient.HttpService;
import one.xingyi.core.httpClient.server.companion.EntityCompanion;
import one.xingyi.reference.address.server.companion.AddressCompanion;
import one.xingyi.reference.person.server.companion.PersonCompanion;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
public class FakeEntityClientTest extends AbstractEntityClientTest {
//    EntityRegister entityRegister = EntityRegister.apply(EntityCompanion.companion, PersonCompanion.companion, AddressCompanion.companion);
//    EndPoint entityEndpoint = EntityDetailsEndpoint.entityDetailsEndPoint(jsonTC, entityRegister, javascriptStore);
    EndPoint entityEndpoints(){return  EndPoint.compose( EntityCompanion.companion.entityEndpoint(), PersonCompanion.companion.entityEndpoint(), AddressCompanion.companion.entityEndpoint());};

    @Override protected Function<ServiceRequest, CompletableFuture<ServiceResponse>> httpClient() { return EndPoint.toKliesli(entityEndpoints()); }
    @Override protected String expectedHost() { return ""; }
}
