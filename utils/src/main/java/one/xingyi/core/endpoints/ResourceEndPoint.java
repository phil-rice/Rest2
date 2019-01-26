package one.xingyi.core.endpoints;

import lombok.RequiredArgsConstructor;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.http.ServiceResponse;
import one.xingyi.core.marshelling.ContextForJson;
import one.xingyi.core.marshelling.HasJson;
import one.xingyi.core.sdk.IXingYiEntity;
import one.xingyi.core.utils.Optionals;
import one.xingyi.core.utils.Strings;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Function;
interface IEndPoint extends Function<ServiceRequest, CompletableFuture<Optional<ServiceResponse>>> {

}

interface IResourceEndpointAcceptor<From> extends Function<ServiceRequest, Optional<From>> {
    static <From> IResourceEndpointAcceptor<From> create(String method, String templatedPath, BiFunction<ServiceRequest, String, From> fromFn) {
        Function<String, Optional<String>> ripper = Strings.ripIdFromPath(templatedPath);
        return new ResourceEndpointAcceptor<>(method, templatedPath, fromFn);
    }
    String method();
    String templatedPath();

    default <T> Function<ServiceRequest, CompletableFuture<Optional<T>>> andIfMatches(Function<From, CompletableFuture<T>> fn) {
        return sr -> Optionals.fold(apply(sr), () -> CompletableFuture.completedFuture(Optional.empty()), from -> fn.apply(from).thenApply(t -> Optional.of(t)));
    }
}

class ResourceEndpointAcceptor<From> implements IResourceEndpointAcceptor<From> {

    final String method;
    final String templatedPath;
    final Function<String, Optional<String>> ripper;
    final BiFunction<ServiceRequest, String, From> fromFn;

    public ResourceEndpointAcceptor(String method, String templatedPath, BiFunction<ServiceRequest, String, From> fromFn) {
        this.method = method;
        this.templatedPath = templatedPath;
        this.ripper = Strings.ripIdFromPath(templatedPath);
        this.fromFn = fromFn;
    }
    @Override public String method() { return method; }
    @Override public String templatedPath() { return templatedPath; }
    @Override public Optional<From> apply(ServiceRequest serviceRequest) {
        if (!serviceRequest.method.equalsIgnoreCase(method)) return Optional.empty();
        return ripper.apply(serviceRequest.url.getPath()).map(id -> fromFn.apply(serviceRequest, id));
    }
}

interface EndpointResult<Result> extends BiFunction<ServiceRequest, Result, ServiceResponse> {
    ServiceResponse apply(ServiceRequest serviceRequest, Result result);
    static <J, Result extends HasJson<ContextForJson>> EndpointResult<Result> create(EndpointContext context, int statusCode) {
        return (sr, r) -> ServiceResponse.jsonString(statusCode, context.resultBody(sr, r));
    }
    static <J, Result extends HasJson<ContextForJson>> EndpointResult<Optional<Result>> createForOptional(EndpointContext context, int statusCodeIfPresent) {
        return (sr, r) -> Optionals.fold(r, () -> ServiceResponse.notFound(""), result -> ServiceResponse.jsonString(statusCodeIfPresent, context.resultBody(sr, result)));
    }
}

interface IResourceEndPoint<J, Entity extends IXingYiEntity, Request, Result> extends EndPoint {

    static <J, Entity extends IXingYiEntity> IResourceEndPoint<J, Entity, String, Optional<Entity>> getEntity(
            EndpointContext<J> context, String templatedPath, Function<String, CompletableFuture<Optional<Entity>>> fn) {
        return new ResourceEndPoint<>(IResourceEndpointAcceptor.<String>create("get", templatedPath, (sr, s) -> s),
                200, fn, EndpointResult.<J, Entity>createForOptional(context, 200));
    }
    static <J, Entity extends IXingYiEntity> IResourceEndPoint<J, Entity, String, Entity> putEntity(
            EndpointContext<J> context, String templatedPath, Function<String, CompletableFuture<Entity>> fn) {
        return new ResourceEndPoint<J, Entity, String, Entity>(IResourceEndpointAcceptor.<String>create("put", templatedPath, (sr, s) -> s),
                201, fn, EndpointResult.<J, Entity>create(context, 200));
    }
}

@RequiredArgsConstructor
public class ResourceEndPoint<J, Entity extends IXingYiEntity, Request, Result> implements IResourceEndPoint<J, Entity, Request, Result> {

    final IResourceEndpointAcceptor<Request> acceptor;
    final int statusCode;
    final Function<Request, CompletableFuture<Result>> fn;
    final EndpointResult<Result> endpointResult;
    @Override public CompletableFuture<Optional<ServiceResponse>> apply(ServiceRequest serviceRequest) {
        return acceptor.andIfMatches(from -> fn.apply(from).thenApply(result -> endpointResult.apply(serviceRequest, result))).apply(serviceRequest);
    }

}