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
import one.xingyi.core.utils.Optionals;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
class JavascriptAndJsonEndPoint<From, To extends HasJson<ContextForJson>> implements EndPoint {
    static public String makeJavascript(JavascriptStore javascriptStore, JavascriptDetailsToString javascriptDetailsToString, AcceptHeaderParser parser, ServiceRequest serviceRequest) {
        List<String> lensNames = serviceRequest.header("accept").map(header -> parser.apply(header).lensNames).orElse(List.of());
        return javascriptDetailsToString.apply(javascriptStore.find(lensNames));
    }

    final AcceptHeaderParser parser = AcceptHeaderParser.parser;
    final JsonWriter<? extends Object> jsonTc;
    final int status;
    final Function<ServiceRequest, Optional<From>> acceptor;
    final Function<From, CompletableFuture<To>> fn;
    final JavascriptStore javascriptStore;
    final JavascriptDetailsToString javascriptDetailsToString;
    final String protocol;


    @Override public CompletableFuture<Optional<ServiceResponse>> apply(ServiceRequest serviceRequest) {
        String javascript = makeJavascript(javascriptStore, javascriptDetailsToString, parser, serviceRequest);

        return Optionals.flip(acceptor.apply(serviceRequest).map(fn)).thenApply(x -> x.map(to -> {
            return ServiceResponse.javascriptAndJson(jsonTc, ContextForJson.forServiceRequest(protocol, serviceRequest), 200, to, javascript);
        }));
    }
}
