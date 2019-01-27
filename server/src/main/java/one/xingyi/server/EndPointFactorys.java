package one.xingyi.server;
import lombok.RequiredArgsConstructor;
import one.xingyi.core.endpoints.*;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.httpClient.EntityDetailsRequest;
import one.xingyi.core.httpClient.client.view.UrlPattern;
import one.xingyi.core.httpClient.server.companion.EntityDetailsCompanion;
import one.xingyi.core.httpClient.server.domain.EntityDetails;
import one.xingyi.core.httpClient.server.domain.IEntityDetails;
import one.xingyi.core.marshelling.ContextForJson;
import one.xingyi.core.marshelling.HasJson;
import one.xingyi.core.sdk.*;
import one.xingyi.core.utils.Lists;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Function;
public interface EndPointFactorys {

    static <J> EndPoint entityEndpointFromContext(EndpointContext<J> context, List<HasBookmarkAndUrl> companions) {
        return IResourceEndPoint.<J, EntityDetails, EntityDetailsRequest, Optional<EntityDetails>>create(
                IResourceEndpointAcceptor.apply("get", "/{id}", (sr, s) -> new EntityDetailsRequest(s)),
                EntityRegister.apply(companions),
                EndpointResult.createForOptional(context, 200));
    }

}
