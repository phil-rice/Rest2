package one.xingyi.core.server;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import one.xingyi.core.endpoints.EndPoint;
import one.xingyi.core.http.Header;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.http.ServiceResponse;
import one.xingyi.core.utils.WrappedException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@ToString
@EqualsAndHashCode

public class EndpointHandler implements HttpHandler {

    final Function<ServiceRequest, CompletableFuture<Optional<ServiceResponse>>> fn;
    private final Function<ServiceRequest, CompletableFuture<ServiceResponse>> kleisli;
    public EndpointHandler(Function<ServiceRequest, CompletableFuture<Optional<ServiceResponse>>> fn) {
        this.fn = fn;
        this.kleisli = EndPoint.toKliesli(fn);
    }

    @Override public void handle(HttpExchange exchange) throws IOException {
        HttpUtils.write(exchange, WrappedException.wrapCallable(() -> kleisli.apply(makeServiceRequest(exchange)).get()));
    }

    ServiceRequest makeServiceRequest(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod().toLowerCase();
        String body = Streams.readAll(exchange.getRequestBody());
        String uri = exchange.getRequestURI().toString();
        Headers req = exchange.getRequestHeaders();
        List<Header> headers = new ArrayList<>();
        for (Map.Entry<String, List<String>> e : req.entrySet()) {
            for (String v : e.getValue())
                headers.add(new Header(e.getKey(), v));
        }
        return new ServiceRequest(method, uri, headers, body);
    }


}
