package one.xingyi.core.endpoints;
import lombok.RequiredArgsConstructor;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.http.ServiceResponse;
import one.xingyi.core.marshelling.ContextForJson;
import one.xingyi.core.mediatype.IMediaTypeServerDefn;
import one.xingyi.core.sdk.IXingYiResource;
import one.xingyi.core.utils.Optionals;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;


@RequiredArgsConstructor
public class EntityMediaTypeEndpoint<Request, Entity extends IXingYiResource> implements IMediaTypeEndpoint<Request, Entity> {
    final IResourceEndpointAcceptor<Request> acceptor;
    final Function<Request, CompletableFuture<Entity>> fn;
    final String protocol;
    final int statusCode;
    final IMediaTypeServerDefn<Entity> mediaType;
    final Function<Entity, String> stateFn;

    @Override public List<MethodPathAndDescription> description() { return acceptor.description(); }

    @Override public CompletableFuture<Optional<ServiceResponse>> apply(ServiceRequest serviceRequest) {
        return Optionals.flip(acceptor.apply(serviceRequest).map(request -> fn.apply(request).thenApply(
                response -> {
                    if (response==null)
                        throw new NullPointerException();
                    return makeServiceResponse(serviceRequest).apply(response);})));
    }
    Function<Entity, ServiceResponse> makeServiceResponse(ServiceRequest serviceRequest) {
        ContextForJson context = ContextForJson.forServiceRequest(protocol, serviceRequest);
        return entity -> new ServiceResponse(statusCode, mediaType.makeDataAndDefn(context, stateFn, entity).asString(), List.of());
    }
}

