package one.xingyi.core.httpClient;


import one.xingyi.core.endpoints.EndPoint;
import one.xingyi.core.endpoints.EndpointAcceptor1;
import one.xingyi.core.httpClient.domain.Entity;
import one.xingyi.core.httpClient.server.companion.EntityCompanion;
import one.xingyi.core.javascript.JavascriptStore;
import one.xingyi.core.marshelling.JsonTC;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public interface EntityDetailsEndpoint {
    EndpointAcceptor1<EntityDetailsRequest> acceptor = EndpointAcceptor1.justOneThing("get", EntityDetailsRequest::new);

    EntityCompanion companion = new EntityCompanion();

    static <J> EndPoint entityDetailsEndPoint(JsonTC<J> jsonTC, EntityRegister entityRegister, JavascriptStore javascriptStore) {
        return EndPoint.javascriptAndJson(jsonTC, 200, acceptor, entityRegister, javascriptStore);
    }
}