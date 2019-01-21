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
public interface EndPointFactory {
    <J> EndPoint create(EndpointContext<J> context);
    static <From, To extends HasJson<ContextForJson>> EndPointFactory bookmarked(String pattern, BiFunction<ServiceRequest, String, From> reqFn, Function<From, CompletableFuture<To>> fn) {
        return new BookmarkedEndpoint<>(pattern, reqFn, fn);
    }
    static <From, To extends HasJson<ContextForJson>> EndPointFactory optionalBookmarked(String pattern, BiFunction<ServiceRequest, String, From> reqFn, Function<From, CompletableFuture<Optional<To>>> fn) {
        return new OptionalBookmarkedEndpoint<>(pattern, reqFn, fn);
    }

}

@RequiredArgsConstructor
class BookmarkedEndpoint< From, To extends HasJson<ContextForJson>> implements EndPointFactory {
    final String pattern;
    final BiFunction<ServiceRequest, String, From> reqFn;
    final Function<From, CompletableFuture<To>> fn;

    @Override public <J>EndPoint create(EndpointContext<J> context) {
        return EndPoint.<J, From, To>javascriptAndJson(
                context.jsonTC,
                200,
                context.protocol,
                EndpointAcceptor1.bookmarkAcceptor("get", pattern, reqFn),
                fn,
                context.javascriptStore);
    }
}
@RequiredArgsConstructor
class OptionalBookmarkedEndpoint<From, To extends HasJson<ContextForJson>> implements EndPointFactory {
    final String pattern;
    final BiFunction<ServiceRequest, String, From> reqFn;
    final Function<From, CompletableFuture<Optional<To>>> fn;

    @Override public <J>EndPoint create(EndpointContext<J> context) {
        return EndPoint.<J, From, To>optionalJavascriptAndJson(
                context.jsonTC,
                200,
                context.protocol,
                EndpointAcceptor1.bookmarkAcceptor("get", pattern, reqFn),
                fn,
                context.javascriptStore);
    }
}
