package one.xingyi.server;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import one.xingyi.core.endpoints.*;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.httpClient.EntityDetailsRequest;
import one.xingyi.core.httpClient.server.domain.EntityDetails;
import one.xingyi.core.utils.Lists;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
public interface EndPointFactorys {

    static <J> EndPoint entityEndpointFromContext(EndpointContext<J> context, List<HasBookmarkAndUrl> companions) {
        EntityRegister entityRegister = EntityRegister.apply(companions);
        return IResourceEndPoint.<J, EntityDetails, EntityDetailsRequest, Optional<EntityDetails>>create(
                new EntityEndpointAcceptor(entityRegister),
                from -> CompletableFuture.completedFuture(entityRegister.apply(from)),
                EndpointResult.createForOptional(context, 200));
    }

}

@ToString
@EqualsAndHashCode
class EntityEndpointAcceptor implements IResourceEndpointAcceptor<EntityDetailsRequest> {
    final EntityRegister entityRegister;
    final List<String> registered;
    public EntityEndpointAcceptor(EntityRegister entityRegister) {
        this.entityRegister = entityRegister;
        this.registered = entityRegister.registered();
    }
    @Override public List<MethodAndPath> description() {
        return Lists.map(registered, r -> new MethodAndPath("get", r));
    }
    @Override public Optional<EntityDetailsRequest> apply(ServiceRequest serviceRequest) {
        if (serviceRequest.method.equalsIgnoreCase("get") && serviceRequest.urlSegments().size() == 2) {
            String entityname = serviceRequest.path;
            if (entityRegister.registered().contains(entityname))
                return Optional.of(new EntityDetailsRequest(entityname));
        }
        return Optional.empty();
    }
    @Override public String method() { return "get"; }
    @Override public String templatedPath() { return "/{entity}"; }
}