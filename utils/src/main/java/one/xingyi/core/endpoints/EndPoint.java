package one.xingyi.core.endpoints;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.http.ServiceResponse;
import one.xingyi.core.marshelling.MakesFromJson;
import one.xingyi.core.sdk.IXingYiEntity;
import one.xingyi.core.utils.IdAndValue;
import one.xingyi.core.utils.Lists;
import one.xingyi.core.utils.Optionals;
import one.xingyi.core.utils.WrappedException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;

public interface EndPoint extends Function<ServiceRequest, CompletableFuture<Optional<ServiceResponse>>>, MethodAndPathDescription {
    List<MethodAndPath> description();


    static Function<ServiceRequest, String> defaultNotFound(EndPoint endPoint) {
        return sr -> "Cannot find response for\n" +
                sr.toString() + "\nLegal Endpoints are\n   " + Lists.mapJoin(endPoint.description(), "\n   ", Objects::toString);
    }

    static <J, Entity extends IXingYiEntity> IResourceEndPoint<J, Entity, String, Optional<Entity>> getOptionalEntity(
            EndpointContext<J> context, String templatedPath, Function<String, CompletableFuture<Optional<Entity>>> fn) {
        return new ResourceEndPoint<>(IResourceEndpointAcceptor.<String>apply("get", templatedPath, (sr, s) -> s),
                fn, EndpointResult.<J, Entity>createForOptional(context, 200));
    }
    static <J, Entity extends IXingYiEntity> IResourceEndPoint<J, Entity, String, Entity> getEntity(
            EndpointContext<J> context, String templatedPath, Function<String, CompletableFuture<Entity>> fn) {
        return new ResourceEndPoint<>(IResourceEndpointAcceptor.<String>apply("get", templatedPath, (sr, s) -> s),
                fn, EndpointResult.<J, Entity>create(context, 200));
    }
    static <J, Entity extends IXingYiEntity> IResourceEndPoint<J, Entity, String, Boolean> deleteEntity(
            EndpointContext<J> context, String templatedPath, Function<String, CompletableFuture<Boolean>> fn) {
        return new ResourceEndPoint<J, Entity, String, Boolean>(IResourceEndpointAcceptor.<String>apply("delete", templatedPath, (sr, s) -> s),
                fn, EndpointResult.<Boolean>create(200, r -> r.toString()));
    }
    static <J, Entity extends IXingYiEntity> IResourceEndPoint<J, Entity, SuccessfulMatch, IdAndValue<Entity>> createEntity(
            EndpointContext<J> context, String path, Supplier<CompletableFuture<IdAndValue<Entity>>> idAndValueSupplier) {
        return new ResourceEndPoint<J, Entity, SuccessfulMatch, IdAndValue<Entity>>(IResourceEndpointAcceptor.<String>apply("post", path),
                s -> {System.out.println("in here: " + s); return idAndValueSupplier.get();}, EndpointResult.<J, Entity>createForIdAndvalue(context, 201));
    }
    static <J, Entity extends IXingYiEntity> IResourceEndPoint<J, Entity, String, Entity> createEntityWithId(
            EndpointContext<J> context, String templatedPath, Function<String, CompletableFuture<Entity>> fn) {
        return new ResourceEndPoint<J, Entity, String, Entity>(IResourceEndpointAcceptor.<String>apply("post", templatedPath, (sr, s) -> s),
                fn, EndpointResult.<J, Entity>create(context, 201));
    }
    static <J, Entity extends IXingYiEntity> IResourceEndPoint<J, Entity, String, Entity> postEntity(
            EndpointContext<J> context, String templatedPath, List<String> states, Function<String, CompletableFuture<Entity>> fn) {
        return new ResourceEndPoint<J, Entity, String, Entity>(IResourceEndpointAcceptor.<String>apply("post", templatedPath, (sr, s) -> s),
                fn, EndpointResult.<J, Entity>create(context, 200));
    }

    static <J, Entity extends IXingYiEntity> IResourceEndPoint<J, Entity, IdAndValue<Entity>, Entity> putEntity(
            MakesFromJson<Entity> maker, EndpointContext<J> context, String templatedPath,  Function<IdAndValue<Entity>, CompletableFuture<Entity>> fn) {
        return new ResourceEndPoint<J, Entity, IdAndValue<Entity>, Entity>(IResourceEndpointAcceptor.<IdAndValue<Entity>>apply("put", templatedPath,
                (sr, s) -> new IdAndValue<Entity>(s, maker.fromJson(context.jsonParser, context.jsonParser.parse(sr.body)))),
                fn, EndpointResult.<J, Entity>create(context, 200));
    }

    static Function<ServiceRequest, CompletableFuture<ServiceResponse>> toKliesli(EndPoint original) {
        return toKliesli(original, defaultNotFound(original));
    }
    static Function<ServiceRequest, CompletableFuture<ServiceResponse>> toKliesli(Function<ServiceRequest, CompletableFuture<Optional<ServiceResponse>>> original, Function<ServiceRequest, String> bodyIfNotFound) {
        if (original == null) throw new NullPointerException();
        return sr -> {
            try {
                return original.apply(sr).thenApply(opt -> opt.orElse(ServiceResponse.notFound(bodyIfNotFound.apply(sr)))).exceptionally(EndPoint::defaultErrorHandler);
            } catch (Exception e) {
                return CompletableFuture.completedFuture(defaultErrorHandler(e));
            }
        };
    }
    static ServiceResponse defaultErrorHandler(Throwable e) {
        System.out.println("Dumping error from inside completable future in toKliesli");
        Throwable actual = WrappedException.unWrap(e);
        actual.printStackTrace();
        return internalError(actual);
    }
    static ServiceResponse internalError(Throwable e) {
        return ServiceResponse.html(500, e.getClass().getName() + "\n" + e.getMessage());
    }

    static EndPoint compose(List<EndPoint> endPoints) {return new ComposeEndPoints(endPoints);}


    static EndPoint staticEndpoint(EndpointAcceptor0 acceptor, ServiceResponse serviceResponse) {
        return new StaticEndpoint(acceptor, serviceResponse);
    }

    static EndPoint printlnLog(EndPoint endPoint) {
        return new PrintlnEndpoint(endPoint);
    }
}

@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
class PrintlnEndpoint implements EndPoint {
    final EndPoint endPoint;
    @Override public List<MethodAndPath> description() {
        return endPoint.description();
    }
    @Override public CompletableFuture<Optional<ServiceResponse>> apply(ServiceRequest sr) {
        return endPoint.apply(sr).thenApply(res -> {
            System.out.println(sr);
            System.out.println(res);
            System.out.println();
            return res;
        });
    }
}

@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
class StaticEndpoint implements EndPoint {
    final EndpointAcceptor0 acceptor;
    final ServiceResponse serviceResponse;
    @Override public List<MethodAndPath> description() { return acceptor.description(); }
    @Override public CompletableFuture<Optional<ServiceResponse>> apply(ServiceRequest serviceRequest) {
        return CompletableFuture.completedFuture(Optionals.from(acceptor.apply(serviceRequest), () -> serviceResponse));
    }
}
