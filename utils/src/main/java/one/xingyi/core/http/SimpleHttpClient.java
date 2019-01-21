package one.xingyi.core.http;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
 class SimpleHttpClient<HttpReq, HttpRes> implements Function<ServiceRequest, CompletableFuture<ServiceResponse>> {
    final Function<HttpReq, CompletableFuture<HttpRes>> service;
    final Function<ServiceRequest, HttpReq> toServiceRequest;
    final Function<HttpRes, ServiceResponse> toServiceResponse;

    @Override public CompletableFuture<ServiceResponse> apply(ServiceRequest serviceRequest) {
        return service.apply(toServiceRequest.apply(serviceRequest)).thenApply(toServiceResponse);
    }
}

