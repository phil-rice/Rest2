package one.xingyi.core.marshelling;
import lombok.RequiredArgsConstructor;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.http.ServiceResponse;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
public interface FetchJavascript extends Function<String, CompletableFuture<String>> {
    static FetchJavascript fromService(Function<ServiceRequest, CompletableFuture<ServiceResponse>> service) {return new FetchJavascriptFromService(service);}
    static FetchJavascript asIs() {return javascript -> CompletableFuture.completedFuture(javascript);}
    static FetchJavascript fromServiceIfNeeded(Function<ServiceRequest, CompletableFuture<ServiceResponse>> service) {
        return new FetchJavascriptFromServiceIfNeed(fromService(service), asIs());
    }
}

@RequiredArgsConstructor
class FetchJavascriptFromServiceIfNeed implements FetchJavascript {
    final FetchJavascript fetchJavascriptFromService;
    final FetchJavascript fetchJavascriptAsIs;
    @Override public CompletableFuture<String> apply(String urlOrJavascript) {
        if (urlOrJavascript.startsWith("http") || urlOrJavascript.startsWith("/")) return fetchJavascriptFromService.apply(urlOrJavascript);
        else return fetchJavascriptAsIs.apply(urlOrJavascript);
    }
}
@RequiredArgsConstructor
class FetchJavascriptFromService implements FetchJavascript {
    final Function<ServiceRequest, CompletableFuture<ServiceResponse>> service;
    @Override public CompletableFuture<String> apply(String s) {
        ServiceRequest serviceRequest = new ServiceRequest("get", s, List.of(), "");
        return service.apply(serviceRequest).thenApply(sr -> {checkSr(serviceRequest, sr); return sr.body;});
    }
    void checkSr(ServiceRequest serviceRequest, ServiceResponse sr) {
        if (sr.statusCode > 299) throw new UnexpectedResponse("Trying to get javascript: " + serviceRequest, sr);
    }
}
