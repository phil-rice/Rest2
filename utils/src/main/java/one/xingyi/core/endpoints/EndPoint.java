package one.xingyi.core.endpoints;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.acceptHeader.AcceptHeaderParser;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.http.ServiceResponse;
import one.xingyi.core.javascript.JavascriptDetailsToString;
import one.xingyi.core.javascript.JavascriptStore;
import one.xingyi.core.marshelling.ContextForJson;
import one.xingyi.core.marshelling.HasJson;
import one.xingyi.core.marshelling.JsonWriter;
import one.xingyi.core.sdk.IXingYiEntity;
import one.xingyi.core.utils.IdAndValue;
import one.xingyi.core.utils.Optionals;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;

public interface EndPoint extends Function<ServiceRequest, CompletableFuture<Optional<ServiceResponse>>> {
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
                s -> idAndValueSupplier.get(), EndpointResult.<J, Entity>createForIdAndvalue(context, 201));
    }
    static <J, Entity extends IXingYiEntity> IResourceEndPoint<J, Entity, String, Entity> createEntityWithId(
            EndpointContext<J> context, String templatedPath, Function<String, CompletableFuture<Entity>> fn) {
        return new ResourceEndPoint<J, Entity, String, Entity>(IResourceEndpointAcceptor.<String>apply("put", templatedPath, (sr, s) -> s),
                fn, EndpointResult.<J, Entity>create(context, 201));
    }
    static <J, Entity extends IXingYiEntity> IResourceEndPoint<J, Entity, String, Entity> postEntity(
            EndpointContext<J> context, String templatedPath, List<String> states, Function<String, CompletableFuture<Entity>> fn) {
        return new ResourceEndPoint<J, Entity, String, Entity>(IResourceEndpointAcceptor.<String>apply("post", templatedPath, (sr, s) -> s),
                fn, EndpointResult.<J, Entity>create(context, 200));
    }

    static Function<ServiceRequest, CompletableFuture<ServiceResponse>> toKliesli(Function<ServiceRequest, CompletableFuture<Optional<ServiceResponse>>> original) {
        if (original == null) throw new NullPointerException();
        return sr -> {
            try {
                return original.apply(sr).thenApply(opt -> opt.orElse(ServiceResponse.notFound(sr.toString()))).exceptionally(e -> internalError(e));
            } catch (Exception e) {
                System.out.println("Dumping error from inside completable future in toKliesli");
                e.printStackTrace();
                return CompletableFuture.completedFuture(internalError(e));
            }
        };
    }
    static ServiceResponse internalError(Throwable e) {
        return ServiceResponse.html(500, e.getClass().getName() + "\n" + e.getMessage());
    }


    static <J, From, To extends HasJson<ContextForJson>> EndPoint json(JsonWriter<J> jsonTC, int status, String protocol, EndpointAcceptor1<From> acceptor, Function<From, CompletableFuture<To>> fn) {
        return new JsonEndPoint<>(jsonTC, status, acceptor, fn, protocol);
    }
    static <J, From, To extends HasJson<ContextForJson>> EndPoint javascriptAndJson
            (JsonWriter<J> jsonTC, int status, String protocol, Function<ServiceRequest, Optional<From>> acceptor, Function<From, CompletableFuture<To>> fn, JavascriptStore javascriptStore) {
        return new JavascriptAndJsonEndPoint<From, To>(jsonTC, status, acceptor, fn, javascriptStore, JavascriptDetailsToString.simple, protocol);
    }
    static <J, From, To extends HasJson<ContextForJson>> EndPoint optionalJavascriptAndJson
            (JsonWriter<J> jsonTC, int status, String protocol, EndpointAcceptor1<From> acceptor, Function<From, CompletableFuture<Optional<To>>> fn, JavascriptStore javascriptStore) {
        return new OptionalJavascriptAndJsonEndPoint<From, To>(jsonTC, status, acceptor, fn, javascriptStore, JavascriptDetailsToString.simple, protocol);
    }


    static EndPoint compose(List<EndPoint> endPoints) {return new ComposeEndPoints(endPoints);}


    static EndPoint staticEndpoint(EndpointAcceptor0 acceptor, ServiceResponse serviceResponse) {
        return sr -> CompletableFuture.completedFuture(Optionals.from(acceptor.apply(sr), () -> serviceResponse));
    }

    static EndPoint printlnLog(EndPoint endPoint) {
        return sr -> endPoint.apply(sr).thenApply(res -> {
            System.out.println(sr);
            System.out.println(res);
            System.out.println();
            return res;
        });

    }
}
