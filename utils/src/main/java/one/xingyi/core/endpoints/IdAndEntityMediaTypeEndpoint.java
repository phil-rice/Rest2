package one.xingyi.core.endpoints;
import lombok.RequiredArgsConstructor;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.http.ServiceResponse;
import one.xingyi.core.marshelling.ContextForJson;
import one.xingyi.core.mediatype.IMediaTypeServerDefn;
import one.xingyi.core.sdk.IXingYiResource;
import one.xingyi.core.utils.IdAndValue;
import one.xingyi.core.utils.Optionals;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
@RequiredArgsConstructor
public class IdAndEntityMediaTypeEndpoint<J, Request, Entity extends IXingYiResource> implements IMediaTypeEndpoint<J, Request, Entity> {
    final IResourceEndpointAcceptor<Request> acceptor;
    final Function<Request, CompletableFuture<IdAndValue<Entity>>> fn;
    final String protocol;
    final int statusCode;
    final IMediaTypeServerDefn<Entity> mediaType;
    final Function<Entity, String> stateFn;

    @Override public List<MethodAndPath> description() { return acceptor.description(); }

    @Override public CompletableFuture<Optional<ServiceResponse>> apply(ServiceRequest serviceRequest) {
        return Optionals.flip(acceptor.apply(serviceRequest).map(request -> fn.apply(request).thenApply(makeServiceResponse(serviceRequest))));
    }
    Function<IdAndValue<Entity>, ServiceResponse> makeServiceResponse(ServiceRequest serviceRequest) {
        ContextForJson context = ContextForJson.forServiceRequest(protocol, serviceRequest);
        return idAndvalue -> new ServiceResponse(statusCode, mediaType.makeDataAndDefn(context, stateFn, idAndvalue).asString(), List.of());
    }
}
