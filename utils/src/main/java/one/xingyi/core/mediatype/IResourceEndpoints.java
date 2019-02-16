package one.xingyi.core.mediatype;
import lombok.RequiredArgsConstructor;
import one.xingyi.core.endpoints.*;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.sdk.IXingYiResource;
import one.xingyi.core.utils.IdAndValue;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
public interface IResourceEndpoints<Entity extends IXingYiResource> {

    EndPoint put(Function<String, CompletableFuture<IdAndValue<Entity>>> fn);
    EndPoint getOptional(Function<String, CompletableFuture<Optional<Entity>>> fn);
    EndPoint get(Function<String, CompletableFuture<Entity>> fn);
    EndPoint delete(Function<String, CompletableFuture<Boolean>> fn);
    EndPoint createWithId(Function<String, CompletableFuture<Entity>> fn);
    EndPoint createWithoutId(String path, Function<ServiceRequest, Entity> fromFn, Function<Entity, CompletableFuture<IdAndValue<Entity>>> createFn);
}

@RequiredArgsConstructor
class MediaTypeResourceEndpoints<Entity extends IXingYiResource> implements IResourceEndpoints<Entity> {
    final String protocol;
    final IMediaTypeServerDefn<Entity> mediaTypeServerDefn;
    final BookmarkCodeAndUrlPattern bookmarkCodeAndUrlPattern;
    final Function<Entity, String> stateFn;

    @Override public EndPoint put(Function<String, CompletableFuture<IdAndValue<Entity>>> fn) {
        return new IdAndEntityMediaTypeEndpoint<>(
                IResourceEndpointAcceptor.<String>apply("put", bookmarkCodeAndUrlPattern.urlPattern, (sr, s) -> s),
                fn, protocol, 200, mediaTypeServerDefn, stateFn);
    }
    @Override public EndPoint getOptional(Function<String, CompletableFuture<Optional<Entity>>> fn) {
        return new OptionalEntityMediaTypeEndpoint<>(IResourceEndpointAcceptor.<String>apply("get", bookmarkCodeAndUrlPattern.urlPattern, (sr, s) -> s),
                fn, protocol, 200, mediaTypeServerDefn, stateFn);
    }
    @Override public EndPoint get(Function<String, CompletableFuture<Entity>> fn) {
        return new EntityMediaTypeEndpoint<>(IResourceEndpointAcceptor.<String>apply("get", bookmarkCodeAndUrlPattern.urlPattern, (sr, s) -> s),
                fn, protocol, 200, mediaTypeServerDefn, stateFn);
    }
    @Override public EndPoint delete(Function<String, CompletableFuture<Boolean>> fn) {
        return IResourceEndPoint.create(IResourceEndpointAcceptor.<String>apply("delete", bookmarkCodeAndUrlPattern.urlPattern, (sr, s) -> s),
                fn, EndpointResult.<Boolean>createForNonEntity(200, r -> r.toString()));
    }
    @Override public EndPoint createWithId(Function<String, CompletableFuture<Entity>> fn) {
        return new EntityMediaTypeEndpoint<>(IResourceEndpointAcceptor.<String>apply("post", bookmarkCodeAndUrlPattern.urlPattern, (sr, s) -> s),
                fn, protocol, 200, mediaTypeServerDefn, stateFn);

    }
    @Override public EndPoint createWithoutId(String path, Function<ServiceRequest, Entity> fromFn, Function<Entity, CompletableFuture<IdAndValue<Entity>>> createFn) {
        return new IdAndEntityMediaTypeEndpoint<Entity, Entity>(IResourceEndpointAcceptor.<Entity>create("post", path, fromFn),
                createFn, protocol, 201, mediaTypeServerDefn, stateFn);
    }
}