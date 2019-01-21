package one.xingyi.core.httpClient;
import lombok.RequiredArgsConstructor;
import one.xingyi.core.endpoints.*;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.httpClient.domain.Entity;
import one.xingyi.core.marshelling.ContextForJson;
import one.xingyi.core.marshelling.HasJson;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Function;
public interface EndPointFactory<J> extends Function<EndpointContext<J>, EndPoint> {
    static <J, From, To extends HasJson<ContextForJson>> EndPointFactory<J> bookmarked(String pattern, BiFunction<ServiceRequest,String, From> reqFn, Function<From, CompletableFuture<To>> fn) {
        return new BookmarkedEndpoint<J, From, To>(pattern, reqFn, fn);
    }
    static <J, From, To extends HasJson<ContextForJson>> EndPointFactory<J> optionalBookmarked(String pattern, BiFunction<ServiceRequest,String, From> reqFn, Function<From, CompletableFuture<Optional<To>>> fn) {
        return new OptionalBookmarkedEndpoint<>(pattern, reqFn, fn);
    }

}

@RequiredArgsConstructor
class BookmarkedEndpoint<J, From, To extends HasJson<ContextForJson>> implements EndPointFactory<J> {
    final String pattern;
    final BiFunction<ServiceRequest,String, From> reqFn;
    final Function<From, CompletableFuture<To>> fn;

    @Override public EndPoint apply(EndpointContext<J> context) {
        return EndPoint.<J, From, To>javascriptAndJson(
                context.jsonTC,
                200,
                EndpointAcceptor1.bookmarkAcceptor("get", pattern, reqFn),
                fn,
                context.javascriptStore);
    }
}
@RequiredArgsConstructor
class OptionalBookmarkedEndpoint<J, From, To extends HasJson<ContextForJson>> implements EndPointFactory<J> {
    final String pattern;
    final BiFunction<ServiceRequest, String, From> reqFn;
    final Function<From, CompletableFuture<Optional<To>>> fn;

    @Override public EndPoint apply(EndpointContext<J> context) {
        return EndPoint.<J, From, To>optionalJavascriptAndJson(
                context.jsonTC,
                200,
                EndpointAcceptor1.bookmarkAcceptor("get", pattern, reqFn),
                fn,
                context.javascriptStore);
    }
}
