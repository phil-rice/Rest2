package one.xingyi.core;
import one.xingyi.core.endpoints.EndPoint;
import one.xingyi.core.endpoints.EndpointConfig;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.http.ServiceResponse;
import one.xingyi.core.httpClient.HttpServiceCompletableFuture;
import one.xingyi.core.sdk.*;
import one.xingyi.core.utils.BiConsumerWithException;
import one.xingyi.core.utils.IdAndValue;
import one.xingyi.json.Json;
import org.junit.Test;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
public abstract class AbstractResourceTest<Resource extends IXingYiResource, ClientResource extends IXingYiClientResource, View extends IXingYiView<ClientResource>, Server extends IXingYiServer> {
    protected EndpointConfig<Object> config = EndpointConfig.defaultConfig(new Json());

    protected String id() {return "someId";}
    protected String newId() {return "someNewId";}
    abstract protected Server server();
    abstract protected IXingYiRemoteAccessDetails<ClientResource, View> accessDetails();
    abstract protected String getItem(View view);
    abstract protected View withItem(View view, String item);

    protected String startItem() {return "startItem";}
    protected String secondItem() {return "secondItem";}

    protected void setup(BiConsumerWithException<HttpServiceCompletableFuture, Server> block) throws Exception {
        Server server = server();
        Function<ServiceRequest, CompletableFuture<ServiceResponse>> serverKleisli = EndPoint.toKliesli(EndPoint.compose(server.allEndpoints(), true));
        HttpServiceCompletableFuture service = HttpServiceCompletableFuture.lensService("http://localhost:9000", config.parserAndWriter, serverKleisli);
        block.accept(service, server);
    }

    @Test public void testGet() throws Exception {
        setup((service, server) -> {
            assertEquals(startItem(), service.get(accessDetails(), id(), this::getItem).get());
        });
    }
    @Test public void testGetOptional() throws Exception {
        setup((service, server) -> {
            assertEquals(Optional.of(startItem()), service.getOptional(accessDetails(), id(), this::getItem).get());
            assertEquals(Optional.empty(), service.getOptional(accessDetails(), newId(), this::getItem).get());
        });
    }

    @Test public void testCreateWithoutId() throws Exception {
        setup((service, server) -> {
            View prototype = service.get(accessDetails(), id(), Function.identity()).get();
            View newView = withItem(prototype, secondItem());
            IdAndValue<View> idAndValue = service.createWithoutId(accessDetails(), newView).get();
            assertFalse(idAndValue.id.equals(id()));
        });
    }

    @Test public void testPrototype() throws Exception {
        setup((service, server) -> {
            View created = service.prototype(accessDetails(), "prototype", id(), v -> withItem(v, startItem())).get();
            assertEquals(startItem(), service.get(accessDetails(), id(), this::getItem).get());
        });
    }

    @Test public void testDelete() throws Exception {
        setup((service, server) -> {
            View view = service.create(accessDetails(), newId()).get();
            service.delete(accessDetails(), newId()).get();
            assertEquals(Optional.empty(), service.getOptional(accessDetails(), newId(), Function.identity()).get());
        });
    }
    @Test public void testEdit() throws Exception {
        setup((service, server) -> {
            service.edit(accessDetails(), id(), v -> withItem(v, secondItem())).get();
            assertEquals(Optional.of(secondItem()), service.getOptional(accessDetails(), id(), Function.identity()).get().map(this::getItem));
        });
    }
}
