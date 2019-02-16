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
public class OptionalEntityMediaTypeEndpoint< Request, Entity extends IXingYiResource> implements IMediaTypeEndpoint<Request, Entity> {
    final IResourceEndpointAcceptor<Request> acceptor;
    final Function<Request, CompletableFuture<Optional<Entity>>> fn;
    final String protocol;
    final int statusCode;
    final IMediaTypeServerDefn<Entity> mediaType;
    final Function<Entity, String> stateFn;

    @Override public List<MethodAndPath> description() { return acceptor.description(); }

    @Override public CompletableFuture<Optional<ServiceResponse>> apply(ServiceRequest serviceRequest) {
        return Optionals.flip(acceptor.apply(serviceRequest).map(request -> fn.apply(request).thenApply(makeServiceResponse(serviceRequest))));
    }
    Function<Optional<Entity>, ServiceResponse> makeServiceResponse(ServiceRequest serviceRequest) {
        ContextForJson context = ContextForJson.forServiceRequest(protocol, serviceRequest);
        return opt -> Optionals.<Entity, ServiceResponse>fold(opt,
                () -> ServiceResponse.notFound("Couldn't find for " + serviceRequest),
                e -> new ServiceResponse(statusCode, mediaType.makeDataAndDefn(context, stateFn, e).asString(), List.of()));
    }
}
