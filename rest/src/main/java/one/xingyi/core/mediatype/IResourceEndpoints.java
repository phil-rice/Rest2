package one.xingyi.core.mediatype;
import lombok.RequiredArgsConstructor;
import one.xingyi.core.endpoints.*;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.sdk.IXingYiResource;
import one.xingyi.core.utils.IdAndValue;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface IResourceEndpoints<Entity extends IXingYiResource> {
    EndPoint put(BiFunction<ServiceRequest, String, IdAndValue<Entity>> fromFn, Function<IdAndValue<Entity>, CompletableFuture<Entity>> fn);
    EndPoint getOptional(Function<String, CompletableFuture<Optional<Entity>>> fn);
    EndPoint get(Function<String, CompletableFuture<Entity>> fn);
    EndPoint delete(Function<String, CompletableFuture<Boolean>> fn);
    EndPoint createWithId(Function<String, CompletableFuture<Entity>> fn);
    EndPoint createWithoutId(String path, Function<ServiceRequest, Entity> fromFn, Function<Entity, CompletableFuture<IdAndValue<Entity>>> createFn);
    EndPoint post(String path, List<String> validStates, Function<String, CompletableFuture<Entity>> postFn);
}

@RequiredArgsConstructor
class MediaTypeResourceEndpoints<Entity extends IXingYiResource> implements IResourceEndpoints<Entity> {
    final String protocol;
    final IMediaTypeServerDefn<Entity> mediaTypeServerDefn;
    final BookmarkCodeAndUrlPattern bookmarkCodeAndUrlPattern;
    final Function<Entity, String> stateFn;

    @Override public EndPoint put(BiFunction<ServiceRequest, String, IdAndValue<Entity>> fromFn, Function<IdAndValue<Entity>, CompletableFuture<Entity>> fn) {
        return new EntityMediaTypeEndpoint<IdAndValue<Entity>, Entity>(
                IResourceEndpointAcceptor.<IdAndValue<Entity>>apply("put", bookmarkCodeAndUrlPattern.urlPattern, fromFn, MediaTypeResourceEndpoints.class.getSimpleName() +"/put"),
                fn, protocol, 200, mediaTypeServerDefn, stateFn);
    }
    @Override public EndPoint getOptional(Function<String, CompletableFuture<Optional<Entity>>> fn) {
        return new OptionalEntityMediaTypeEndpoint<>(IResourceEndpointAcceptor.<String>apply("get", bookmarkCodeAndUrlPattern.urlPattern, (sr, s) -> s, MediaTypeResourceEndpoints.class.getSimpleName() +"/getOptional"),
                fn, protocol, 200, mediaTypeServerDefn, stateFn);
    }
    @Override public EndPoint get(Function<String, CompletableFuture<Entity>> fn) {
        return new EntityMediaTypeEndpoint<>(IResourceEndpointAcceptor.<String>apply("get", bookmarkCodeAndUrlPattern.urlPattern, (sr, s) -> s, MediaTypeResourceEndpoints.class.getSimpleName() +"/get"),
                fn, protocol, 200, mediaTypeServerDefn, stateFn);
    }
    @Override public EndPoint delete(Function<String, CompletableFuture<Boolean>> fn) {
        return IResourceEndPoint.create(IResourceEndpointAcceptor.<String>apply("delete", bookmarkCodeAndUrlPattern.urlPattern, (sr, s) -> s, MediaTypeResourceEndpoints.class.getSimpleName() +"/delete"),
                fn, EndpointResult.<Boolean>createForNonEntity(200, r -> r.toString()));
    }
    @Override public EndPoint createWithId(Function<String, CompletableFuture<Entity>> fn) {
        return new EntityMediaTypeEndpoint<>(IResourceEndpointAcceptor.<String>apply("post", bookmarkCodeAndUrlPattern.urlPattern, (sr, s) -> s, MediaTypeResourceEndpoints.class.getSimpleName() +"/createWithId"),
                fn, protocol, 200, mediaTypeServerDefn, stateFn);

    }
    @Override public EndPoint createWithoutId(String path, Function<ServiceRequest, Entity> fromFn, Function<Entity, CompletableFuture<IdAndValue<Entity>>> createFn) {
        return new IdAndEntityMediaTypeEndpoint<Entity, Entity>(IResourceEndpointAcceptor.<Entity>create("post", path, fromFn, MediaTypeResourceEndpoints.class.getSimpleName() +"/createWithoutId"),
                createFn, protocol, 201, mediaTypeServerDefn, stateFn);
    }
    @Override public EndPoint post(String path, List<String> validStates, Function<String, CompletableFuture<Entity>> postFn) {
        return new EntityMediaTypeEndpoint<String, Entity>(
                IResourceEndpointAcceptor.<String>apply("post", path, (sr, s) -> s, MediaTypeResourceEndpoints.class.getSimpleName() +"/post"),
                postFn, protocol, 200, mediaTypeServerDefn, stateFn);
    }
}