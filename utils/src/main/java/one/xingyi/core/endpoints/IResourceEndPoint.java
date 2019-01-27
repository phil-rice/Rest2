package one.xingyi.core.endpoints;
import lombok.RequiredArgsConstructor;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.http.ServiceResponse;
import one.xingyi.core.sdk.IXingYiEntity;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
public interface IResourceEndPoint<J, Entity extends IXingYiEntity, Request, Result> extends EndPoint {

}


@RequiredArgsConstructor
class ResourceEndPoint<J, Entity extends IXingYiEntity, Request, Result> implements IResourceEndPoint<J, Entity, Request, Result> {

    final IResourceEndpointAcceptor<Request> acceptor;
    final Function<Request, CompletableFuture<Result>> fn;
    final EndpointResult<Result> endpointResult;
    @Override public CompletableFuture<Optional<ServiceResponse>> apply(ServiceRequest serviceRequest) {
        return acceptor.andIfMatches(from -> fn.apply(from).thenApply(result -> endpointResult.apply(serviceRequest, result))).apply(serviceRequest);
    }

    @Override public List<MethodAndPath> description() { return acceptor.description(); }
}
