package one.xingyi.core;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import one.xingyi.core.endpoints.*;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.httpClient.ResourceDetailsRequest;
import one.xingyi.core.httpClient.server.companion.ResourceDetailsCompanion;
import one.xingyi.core.httpClient.server.domain.ResourceDetails;
import one.xingyi.core.mediatype.IXingYiServerMediaTypeDefn;
import one.xingyi.core.utils.Lists;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
public interface EndPointFactorys {

    static <J> EndPoint entityEndpointFromContext(EndpointContext<J> context, List<HasBookmarkAndUrl> companions) {
        IXingYiServerMediaTypeDefn<ResourceDetails> mediaTypeDefn = ResourceDetailsCompanion.companion.lensMediaDefn(context);
        EntityRegister entityRegister = EntityRegister.apply(companions);
        Function<ResourceDetails, String> noStateFn = x -> "";
        return IResourceEndPoint.< ResourceDetails, ResourceDetailsRequest, Optional<ResourceDetails>>create(
                new EntityEndpointAcceptor(entityRegister),
                from -> CompletableFuture.completedFuture(entityRegister.apply(from)),
                EndpointResult.createForOptional(context, 200, ResourceDetailsCompanion.companion.bookmarkAndUrl().code));
    }

}

@ToString
@EqualsAndHashCode
class EntityEndpointAcceptor implements IResourceEndpointAcceptor<ResourceDetailsRequest> {
    final EntityRegister entityRegister;
    final List<String> registered;
    public EntityEndpointAcceptor(EntityRegister entityRegister) {
        this.entityRegister = entityRegister;
        this.registered = entityRegister.registered();
    }
    @Override public List<MethodPathAndDescription> description() {
        return Lists.map(registered, r -> new MethodPathAndDescription("get", r, EntityEndpointAcceptor.class.getSimpleName()));
    }
    @Override public Optional<ResourceDetailsRequest> apply(ServiceRequest serviceRequest) {
        if (serviceRequest.method.equalsIgnoreCase("get") && serviceRequest.urlSegments().size() == 2) {
            String entityname = serviceRequest.path;
            if (entityRegister.registered().contains(entityname))
                return Optional.of(new ResourceDetailsRequest(entityname));
        }
        return Optional.empty();
    }
    @Override public String method() { return "get"; }
    @Override public String templatedPath() { return "/{entity}"; }
}