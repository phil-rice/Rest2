package one.xingyi.core.endpoints;
import lombok.RequiredArgsConstructor;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.http.ServiceResponse;
import one.xingyi.core.sdk.IXingYiResource;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
public interface IResourceEndPoint< Entity extends IXingYiResource, Request, Result> extends EndPoint {
    public static <Entity extends IXingYiResource, Request, Result> IResourceEndPoint< Entity, Request, Result>
    create(IResourceEndpointAcceptor<Request> acceptor, Function<Request, CompletableFuture<Result>> fn, EndpointResult<Result> endpointResult) {
        return new ResourceEndPoint<Entity, Request, Result>(acceptor, fn, endpointResult);
    }

}
@RequiredArgsConstructor
class ResourceEndPoint< Entity extends IXingYiResource, Request, Result> implements IResourceEndPoint<Entity, Request, Result> {

    final IResourceEndpointAcceptor<Request> acceptor;
    final Function<Request, CompletableFuture<Result>> fn;
    final EndpointResult<Result> endpointResult;
    @Override public CompletableFuture<Optional<ServiceResponse>> apply(ServiceRequest serviceRequest) {
        return acceptor.andIfMatches(from -> fn.apply(from).thenApply(result ->
                endpointResult.apply(serviceRequest, result))).
                apply(serviceRequest);
    }

    @Override public List<MethodAndPath> description() { return acceptor.description(); }
}
