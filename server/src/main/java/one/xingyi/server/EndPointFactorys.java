package one.xingyi.server;
import lombok.RequiredArgsConstructor;
import one.xingyi.core.endpoints.*;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.httpClient.EntityDetailsRequest;
import one.xingyi.core.httpClient.server.companion.EntityDetailsCompanion;
import one.xingyi.core.marshelling.ContextForJson;
import one.xingyi.core.marshelling.HasJson;
import one.xingyi.core.sdk.IXingYiServerCompanion;
import one.xingyi.core.utils.Lists;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Function;
public interface EndPointFactorys {


    static <J> EndPoint entityEndpoint(EndpointConfig<J> config, List<HasBookmarkAndUrl> companions) {
        EntityRegister entityRegister = EntityRegister.apply(companions);
        EndPointFactory entityFactory = EndPointFactorys.optionalBookmarked("/<id>", EntityDetailsRequest::create, entityRegister);
        return entityFactory.create(config.from(List.of(EntityDetailsCompanion.companion)));
    }

    static <J> EndPoint companionEndpoint(EndpointConfig<J> config, List<GetEntityEndpointDetails<?, ?>> details, List<IXingYiServerCompanion<?, ?>> companionsWithoutEndpoint) {
        EndpointContext<J> context = config.from(Lists.append(Lists.map(details, d -> (IXingYiServerCompanion<?, ?>) d.companion), companionsWithoutEndpoint));
        List<EndPointFactory> factories = Lists.map(details, GetEntityEndpointDetails::make);
        return EndPoint.compose(Lists.map(factories, f -> f.create(context)));

    }
    static <J> EndPoint create(EndpointConfig<J> config, List<GetEntityEndpointDetails<?, ?>> details, List<IXingYiServerCompanion<?, ?>> companionsWithoutEndpoint) {
        return EndPoint.compose(List.of(
                entityEndpoint(config, Lists.map(details, d -> d.companion)),
                companionEndpoint(config, details, companionsWithoutEndpoint)));
    }

    static <From, To extends HasJson<ContextForJson>> EndPointFactory bookmarked(String pattern, BiFunction<ServiceRequest, String, From> reqFn, Function<From, CompletableFuture<To>> fn) {
        return new BookmarkedEndpoint<>(pattern, reqFn, fn);
    }
    static <From, To extends HasJson<ContextForJson>> EndPointFactory optionalBookmarked(String pattern, BiFunction<ServiceRequest, String, From> reqFn, Function<From, CompletableFuture<Optional<To>>> fn) {
        return new OptionalBookmarkedEndpoint<>(pattern, reqFn, fn);
    }

}
@RequiredArgsConstructor
class BookmarkedEndpoint<From, To extends HasJson<ContextForJson>> implements EndPointFactory {
    final String pattern;
    final BiFunction<ServiceRequest, String, From> reqFn;
    final Function<From, CompletableFuture<To>> fn;

    @Override public <J> EndPoint create(EndpointContext<J> context) {
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

    @Override public <J> EndPoint create(EndpointContext<J> context) {
        return EndPoint.<J, From, To>optionalJavascriptAndJson(
                context.jsonTC,
                200,
                context.protocol,
                EndpointAcceptor1.bookmarkAcceptor("get", pattern, reqFn),
                fn,
                context.javascriptStore);
    }
}
