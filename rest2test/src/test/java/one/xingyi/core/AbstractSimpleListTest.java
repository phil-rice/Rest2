package one.xingyi.core;
import one.xingyi.core.client.ISimpleList;
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

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
//Starts with a list of just item1
public abstract class AbstractSimpleListTest<Resource extends IXingYiResource, ClientResource extends IXingYiClientResource, View extends IXingYiView<ClientResource>, Server extends IXingYiServer, T> {
    protected EndpointConfig<Object> config = EndpointConfig.defaultConfig(new Json());

    protected String id() {return "someId";}
    protected String newId() {return "someNewId";}
    abstract protected Server server();
    abstract protected IXingYiRemoteAccessDetails<ClientResource, View> accessDetails();

    abstract protected ISimpleList<T> getItem(View view);
    abstract protected View withItem(View view, ISimpleList<T> item);

    protected abstract T item1();
    protected abstract T item2();
    protected abstract T item3();
    ISimpleList<T> list123() {return ISimpleList.fromList(List.of(item1(), item2(), item3())); }

    protected void setup(BiConsumerWithException<HttpServiceCompletableFuture, Server> block) throws Exception {
        Server server = server();
        Function<ServiceRequest, CompletableFuture<ServiceResponse>> serverKleisli = EndPoint.toKliesli(EndPoint.compose(server.allEndpoints(), true));
        HttpServiceCompletableFuture service = HttpServiceCompletableFuture.lensService("http://localhost:9000", config.parserAndWriter, serverKleisli);
        block.accept(service, server);
    }

    @Test public void testGet() throws Exception {
        setup((service, server) -> {
            assertEquals(ISimpleList.fromList(List.of(item1())), service.get(accessDetails(), id(), this::getItem));
            assertEquals(item1(), service.get(accessDetails(), id(), i -> getItem(i).get(0)));
            assertEquals(1, service.get(accessDetails(), id(), i -> getItem(i).size()));
        });
    }
    @Test public void testGetOptional() throws Exception {
        setup((service, server) -> {
            assertEquals(Optional.of(ISimpleList.fromList(List.of(item1()))), service.getOptional(accessDetails(), id(), this::getItem).get());
            assertEquals(Optional.empty(), service.getOptional(accessDetails(), newId(), this::getItem).get());
            assertEquals(item1(), service.getOptional(accessDetails(), id(), i -> getItem(i).get(0)).get());
            assertEquals(1, service.getOptional(accessDetails(), id(), i -> getItem(i).size()).get());
        });
    }

    @Test public void testCreateWithoutId() throws Exception {
        setup((service, server) -> {
            View prototype = service.get(accessDetails(), id(), Function.identity()).get();
            View newView = withItem(prototype, list123());
            IdAndValue<View> idAndValue = service.createWithoutId(accessDetails(), newView).get();
            assertFalse(idAndValue.id.equals(id()));
        });
    }

    @Test public void testPrototype() throws Exception {
        setup((service, server) -> {
            View created = service.prototype(accessDetails(), "prototype", id(), v -> withItem(v, list123())).get();
            assertEquals(list123(), service.get(accessDetails(), id(), this::getItem).get());
        });
    }

    @Test public void testDelete() throws Exception {
        setup((service, server) -> {
            View view = service.create(accessDetails(), newId()).get();
            service.delete(accessDetails(), newId()).get();
            assertEquals(Optional.empty(), service.getOptional(accessDetails(), newId(), Function.identity()).get());
        });
    }
    @Test public void testEditModifyingItems() throws Exception {
        setup((service, server) -> {
            service.edit(accessDetails(), id(), v -> withItem(v, getItem(v).withItem(0, item2()))).get();
            assertEquals(ISimpleList.fromList(List.of(item2())), getItem(service.get(accessDetails(), id(), Function.identity()).get()));
        });
    }
}
