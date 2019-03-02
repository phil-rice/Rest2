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
    final List<EndPoint> endpoints;
    final boolean debug;

    public ComposeEndPoints(List<EndPoint> endpoints, boolean debug) {
        this.endpoints = endpoints;
        this.debug = debug;
        for (EndPoint endPoint : endpoints)
            if (endPoint == null) throw new NullPointerException(endpoints.toString());
    }

    CompletableFuture<Optional<ServiceResponse>> recurse(ServiceRequest serviceRequest, int index) {
        if (index >= endpoints.size()) {
            if (debug)
                System.out.println("Could not find " + serviceRequest + "\n" + Lists.mapJoin(endpoints, "\n", e -> e.description().toString()));
            return CompletableFuture.completedFuture(Optional.empty());
        }
        EndPoint endPoint = endpoints.get(index);
        if (debug)
            System.out.println("Checking end point " + endPoint.description() + " with " + serviceRequest);
        return endPoint.apply(serviceRequest).thenCompose(op -> {
            if (op.isEmpty()) { return recurse(serviceRequest, index + 1); } else {
                if (debug) System.out.println("...... found it" + endPoint.description() + " for " + serviceRequest);
                return CompletableFuture.completedFuture(op);
            }
        });

    }

    @Override public CompletableFuture<Optional<ServiceResponse>> apply(ServiceRequest serviceRequest) {
        return recurse(serviceRequest, 0);
    }
    @Override public List<MethodPathAndDescription> description() { return Lists.flatMap(endpoints, EndPoint::description); }
}
