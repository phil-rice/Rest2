package one.xingyi.core.endpoints;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.http.ServiceResponse;
import one.xingyi.core.utils.Lists;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
@ToString
@EqualsAndHashCode
class ComposeEndPoints implements EndPoint {
    public ComposeEndPoints(List<EndPoint> endpoints) {
        this.endpoints = endpoints;
        for (EndPoint endPoint : endpoints)
            if (endPoint == null) throw new NullPointerException(endpoints.toString());
    }
    final List<EndPoint> endpoints;

    CompletableFuture<Optional<ServiceResponse>> recurse(ServiceRequest serviceRequest, int index) {
        if (index >= endpoints.size())
            return CompletableFuture.completedFuture(Optional.empty());
        EndPoint endPoint = endpoints.get(index);
        return endPoint.apply(serviceRequest).thenCompose(op -> {
            if (op.isEmpty()) { return recurse(serviceRequest, index + 1); } else { return CompletableFuture.completedFuture(op); }
        });

    }

    @Override public CompletableFuture<Optional<ServiceResponse>> apply(ServiceRequest serviceRequest) {
        return recurse(serviceRequest, 0);
    }
    @Override public List<MethodAndPath> description() { return Lists.flatMap(endpoints, EndPoint::description); }
}
