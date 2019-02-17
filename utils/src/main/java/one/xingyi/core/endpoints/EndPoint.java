package one.xingyi.core.endpoints;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.http.ServiceResponse;
import one.xingyi.core.javascript.JavascriptDetailsToString;
import one.xingyi.core.javascript.JavascriptStore;
import one.xingyi.core.marshelling.ContextForJson;
import one.xingyi.core.marshelling.HasJsonWithLinks;
import one.xingyi.core.marshelling.IXingYiResponseSplitter;
import one.xingyi.core.marshelling.MakesFromJson;
import one.xingyi.core.sdk.IXingYiResource;
import one.xingyi.core.utils.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;

public interface EndPoint extends Function<ServiceRequest, CompletableFuture<Optional<ServiceResponse>>>, MethodAndPathDescription {
    List<MethodAndPath> description();


    static Function<ServiceRequest, String> defaultNotFound(EndPoint endPoint) {
        return sr -> "Cannot find response for\n" +
                sr.toString() + "\nLegal Endpoints are\n   " + Lists.mapJoin(endPoint.description(), "\n   ", Objects::toString);
    }

    static Function<ServiceRequest, CompletableFuture<ServiceResponse>> toKliesli(EndPoint original) {
        return toKliesli(original, defaultNotFound(original));
    }
    static Function<ServiceRequest, CompletableFuture<ServiceResponse>> toKliesli(Function<ServiceRequest, CompletableFuture<Optional<ServiceResponse>>> original, Function<ServiceRequest, String> bodyIfNotFound) {
        if (original == null) throw new NullPointerException();
        return sr -> {
            try {
                return original.apply(sr).thenApply(opt -> opt.orElse(ServiceResponse.notFound(bodyIfNotFound.apply(sr)))).exceptionally(EndPoint::defaultErrorHandler);
            } catch (Exception e) {
                return CompletableFuture.completedFuture(defaultErrorHandler(e));
            }
        };
    }
    static ServiceResponse defaultErrorHandler(Throwable e) {
        System.out.println("Dumping error from inside completable future in toKliesli");
        Throwable actual = WrappedException.unWrap(e);
        actual.printStackTrace();
        return internalError(actual);
    }
    static ServiceResponse internalError(Throwable e) {
        return ServiceResponse.html(500, e.getClass().getName() + "\n" + e.getMessage());
    }

    static EndPoint compose(List<EndPoint> endPoints) {return new ComposeEndPoints(endPoints);}

    static <J> EndPoint javascript(EndpointContext<J> context, String prefix) {
        String javascript = context.javascriptDetailsToString.apply(context.javascriptStore.find(List.of()));
        DigestAndString digestAndString = Digestor.digestor().apply(javascript);
        return new StaticEndpoint(EndpointAcceptor0.exact("get", prefix + "/" + digestAndString.digest),
                new ServiceResponse(200, digestAndString.string, List.of()));
    }

    static EndPoint staticEndpoint(EndpointAcceptor0 acceptor, ServiceResponse serviceResponse) {
        return new StaticEndpoint(acceptor, serviceResponse);
    }

    static EndPoint printlnLog(EndPoint endPoint) {
        return new PrintlnEndpoint(endPoint);
    }
    static EndPoint printlnDetailsLog(EndPoint endPoint) {
        return new PrintlnEndpoint(endPoint);
    }
}

@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
class PrintlnEndpoint implements EndPoint {
    final EndPoint endPoint;
    @Override public List<MethodAndPath> description() {
        return endPoint.description();
    }
    @Override public CompletableFuture<Optional<ServiceResponse>> apply(ServiceRequest sr) {
        return endPoint.apply(sr).thenApply(optRes -> {
            String result = optRes.map(r -> {
                        if (r.body.contains(IXingYiResponseSplitter.marker))
                            return IXingYiResponseSplitter.rawSplit(r).data;
                        else return sr.body;
                    }
            ).orElse("??");
            System.out.println(Strings.padRight(sr.method,5) + Strings.padRight(sr.uri.toString(), 80) + "    " + optRes.map(r -> r.statusCode).orElse(0) + " " + result);
            return optRes;
        });
    }
}

@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
class PrintlnDetailsEndpoint implements EndPoint {
    final EndPoint endPoint;
    @Override public List<MethodAndPath> description() {
        return endPoint.description();
    }
    @Override public CompletableFuture<Optional<ServiceResponse>> apply(ServiceRequest sr) {
        return endPoint.apply(sr).thenApply(res -> {
            System.out.println(sr);
            System.out.println(res);
            System.out.println();
            return res;
        });
    }
}

@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
class StaticEndpoint implements EndPoint {
    final EndpointAcceptor0 acceptor;
    final ServiceResponse serviceResponse;
    @Override public List<MethodAndPath> description() { return acceptor.description(); }
    @Override public CompletableFuture<Optional<ServiceResponse>> apply(ServiceRequest serviceRequest) {

        return CompletableFuture.completedFuture(Optionals.from(acceptor.apply(serviceRequest), () -> serviceResponse));
    }
}
