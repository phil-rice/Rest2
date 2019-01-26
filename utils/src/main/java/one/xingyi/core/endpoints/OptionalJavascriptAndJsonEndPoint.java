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

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;


@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
class OptionalJavascriptAndJsonEndPoint<From, To extends HasJson<ContextForJson>> implements EndPoint {

    final JsonWriter<? extends Object> jsonTc;
    final int status;
    final EndpointAcceptor1<From> acceptor;
    final Function<From, CompletableFuture<Optional<To>>> fn;
    final JavascriptStore javascriptStore;
    final JavascriptDetailsToString javascriptDetailsToString;
    final String protocol;

    @Override public CompletableFuture<Optional<ServiceResponse>> apply(ServiceRequest serviceRequest) {
        Optional<From> optFrom = acceptor.apply(serviceRequest);
        if (optFrom.isEmpty()) return CompletableFuture.completedFuture(Optional.empty());
        String javascript = JavascriptAndJsonEndPoint.makeJavascript(javascriptStore, javascriptDetailsToString, AcceptHeaderParser.parser, serviceRequest);
        From from = optFrom.get();
        return fn.apply(from).thenApply(x -> x.map(to -> ServiceResponse.javascriptAndJson(jsonTc, ContextForJson.forServiceRequest(protocol, serviceRequest), 200, to, javascript)));


    }
}


