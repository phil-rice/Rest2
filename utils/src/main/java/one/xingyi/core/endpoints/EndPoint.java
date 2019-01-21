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
import one.xingyi.core.marshelling.JsonTC;
import one.xingyi.core.utils.Optionals;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
public interface EndPoint extends Function<ServiceRequest, CompletableFuture<Optional<ServiceResponse>>> {

    //    static Function<ServiceRequest, CompletableFuture<ServiceResponse>> wrapWithFakeHostHeader(Function<ServiceRequest, CompletableFuture<Optional<ServiceResponse>>> original) {
//
//    }
    static Function<ServiceRequest, CompletableFuture<ServiceResponse>> toKliesli(Function<ServiceRequest, CompletableFuture<Optional<ServiceResponse>>> original) {
        if (original == null) throw new NullPointerException();
        return sr -> {
            try {
                return original.apply(sr).thenApply(opt -> opt.orElse(ServiceResponse.notFound(sr.toString()))).exceptionally(e -> internalError(e));
            } catch (Exception e) {
                System.out.println("Dumping error from inside completable future in toKliesli");
                e.printStackTrace();
                return CompletableFuture.completedFuture(internalError(e));
            }
        };
    }
    static ServiceResponse internalError(Throwable e) {
        return ServiceResponse.html(500, e.getClass().getName() + "\n" + e.getMessage());
    }


    static <J, From, To extends HasJson<ContextForJson>> EndPoint json(JsonTC<J> jsonTC, int status, EndpointAcceptor1<From> acceptor, Function<From, CompletableFuture<To>> fn) {
        return new JsonEndPoint<>(jsonTC, status, acceptor, fn);
    }
    static <J, From, To extends HasJson<ContextForJson>> EndPoint javascriptAndJson
            (JsonTC<J> jsonTC, int status, Function<ServiceRequest, Optional<From>> acceptor, Function<From, CompletableFuture<To>> fn, JavascriptStore javascriptStore) {
        return new JavascriptAndJsonEndPoint<From, To>(jsonTC, status, acceptor, fn, javascriptStore, JavascriptDetailsToString.simple);
    }
    static <J, From, To extends HasJson<ContextForJson>> EndPoint optionalJavascriptAndJson
            (JsonTC<J> jsonTC, int status, EndpointAcceptor1<From> acceptor, Function<From, CompletableFuture<Optional<To>>> fn, JavascriptStore javascriptStore) {
        return new OptionalJavascriptAndJsonEndPoint<From, To>(jsonTC, status, acceptor, fn, javascriptStore, JavascriptDetailsToString.simple);
    }


    static EndPoint compose(EndPoint... endPoints) {return new ComposeEndPoints(Arrays.asList(endPoints));}
    static EndPoint staticEndpoint(EndpointAcceptor0 acceptor, ServiceResponse serviceResponse) {
        return sr -> CompletableFuture.completedFuture(Optionals.from(acceptor.apply(sr), () -> serviceResponse));
    }
    static EndPoint function(EndpointAcceptor0 acceptor, Function<ServiceRequest, ServiceResponse> fn) {
        return sr -> CompletableFuture.completedFuture(Optionals.from(acceptor.apply(sr), () -> fn.apply(sr)));
    }
    static EndPoint printlnLog(EndPoint endPoint) {
        return sr -> endPoint.apply(sr).thenApply(res -> {
            System.out.println(sr);
            System.out.println(res);
            System.out.println();
            return res;
        });

    }
}


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
//        System.out.println("Evaluating " + endPoint + "\nagainst " + serviceRequest.urlSegments());
        return endPoint.apply(serviceRequest).thenCompose(op -> {
            if (op.isEmpty()) {
//                System.out.println("  didn't match");
                return recurse(serviceRequest, index + 1);
            } else {
//                System.out.println("  matched: " + op);
                return CompletableFuture.completedFuture(op);
            }
        });

    }

    @Override public CompletableFuture<Optional<ServiceResponse>> apply(ServiceRequest serviceRequest) {
        return recurse(serviceRequest, 0);
    }
}

//@ToString
//@EqualsAndHashCode
//@RequiredArgsConstructor
//class SimpleEndPoint<From extends EndpointRequest, To extends EndpointResponse> implements EndPoint {
//
//    final EndpointAcceptor1<From> acceptor;
//    final Function<From, CompletableFuture<To>> fn;
//
//    //wow this is a bit of dogs dinner
//    @Override public CompletableFuture<Optional<ServiceResponse>> apply(ServiceRequest serviceRequest) {
//        return acceptor.apply(serviceRequest).
//                map(from -> fn.apply(from).thenApply(to -> Optional.of(to.serviceResponse()))).
//                orElse(CompletableFuture.completedFuture(Optional.empty()));
//    }
//}
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
class JsonEndPoint<From, To extends HasJson<ContextForJson>> implements EndPoint {

    final JsonTC<? extends Object> jsonTc;
    final int status;
    final EndpointAcceptor1<From> acceptor;
    final Function<From, CompletableFuture<To>> fn;

    //wow this is a bit of dogs dinner
    @Override public CompletableFuture<Optional<ServiceResponse>> apply(ServiceRequest serviceRequest) {
        return Optionals.flip(acceptor.apply(serviceRequest).map(fn)).thenApply(x -> x.map(to -> ServiceResponse.json(jsonTc, ContextForJson.forServiceRequest(serviceRequest), status, to)));
    }
}
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
class JavascriptAndJsonEndPoint<From, To extends HasJson<ContextForJson>> implements EndPoint {
    static public String makeJavascript(JavascriptStore javascriptStore, JavascriptDetailsToString javascriptDetailsToString, AcceptHeaderParser parser, ServiceRequest serviceRequest) {
        List<String> lensNames = serviceRequest.header("accept").map(header -> parser.apply(header).lensNames).orElse(List.of());
        return javascriptDetailsToString.apply(javascriptStore.find(lensNames));
    }

    final AcceptHeaderParser parser = AcceptHeaderParser.parser;
    final JsonTC<? extends Object> jsonTc;
    final int status;
    final Function<ServiceRequest, Optional<From>> acceptor;
    final Function<From, CompletableFuture<To>> fn;
    final JavascriptStore javascriptStore;
    final JavascriptDetailsToString javascriptDetailsToString;


    @Override public CompletableFuture<Optional<ServiceResponse>> apply(ServiceRequest serviceRequest) {
        String javascript = makeJavascript(javascriptStore, javascriptDetailsToString, parser, serviceRequest);

        return Optionals.flip(acceptor.apply(serviceRequest).map(fn)).thenApply(x -> x.map(to -> {
            return ServiceResponse.javascriptAndJson(jsonTc, ContextForJson.forServiceRequest(serviceRequest), 200, to, javascript);
        }));
    }
}
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
class OptionalJavascriptAndJsonEndPoint<From, To extends HasJson<ContextForJson>> implements EndPoint {

    final JsonTC<? extends Object> jsonTc;
    final int status;
    final EndpointAcceptor1<From> acceptor;
    final Function<From, CompletableFuture<Optional<To>>> fn;
    final JavascriptStore javascriptStore;
    final JavascriptDetailsToString javascriptDetailsToString;

    @Override public CompletableFuture<Optional<ServiceResponse>> apply(ServiceRequest serviceRequest) {
        Optional<From> optFrom = acceptor.apply(serviceRequest);
        if (optFrom.isEmpty()) return CompletableFuture.completedFuture(Optional.empty());
        String javascript = JavascriptAndJsonEndPoint.makeJavascript(javascriptStore, javascriptDetailsToString, AcceptHeaderParser.parser, serviceRequest);
        From from = optFrom.get();
        return fn.apply(from).thenApply(x -> x.map(to -> ServiceResponse.javascriptAndJson(jsonTc, ContextForJson.forServiceRequest(serviceRequest), 200, to, javascript)));


    }
}