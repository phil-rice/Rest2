package one.xingyi.core.endpoints;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.http.ServiceResponse;
import one.xingyi.core.marshelling.ContextForJson;
import one.xingyi.core.marshelling.HasJson;
import one.xingyi.core.marshelling.JsonWriter;
import one.xingyi.core.utils.Optionals;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
class JsonEndPoint<From, To extends HasJson<ContextForJson>> implements EndPoint {

    final JsonWriter<? extends Object> jsonTc;
    final int status;
    final EndpointAcceptor1<From> acceptor;
    final Function<From, CompletableFuture<To>> fn;
    final String protocol;

    //wow this is a bit of dogs dinner
    @Override public CompletableFuture<Optional<ServiceResponse>> apply(ServiceRequest serviceRequest) {
        return Optionals.flip(acceptor.apply(serviceRequest).map(fn)).thenApply(x -> x.map(to -> ServiceResponse.json(jsonTc, ContextForJson.forServiceRequest(protocol, serviceRequest), status, to)));
    }
}
