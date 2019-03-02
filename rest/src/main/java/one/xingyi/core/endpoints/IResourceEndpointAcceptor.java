package one.xingyi.core.endpoints;

import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.utils.Optionals;
import one.xingyi.core.utils.Strings;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface IResourceEndpointAcceptor<From> extends Function<ServiceRequest, Optional<From>>, MethodAndPathDescription {
    static <From> IResourceEndpointAcceptor<From> apply(String method, String templatedPath, BiFunction<ServiceRequest, String, From> fromFn, String description) {
//        Function<String, Optional<String>> ripper = Strings.ripIdFromPath(templatedPath.replace("{host}", ""));
        return new ResourceWithFromEndpointAcceptor<>(method, templatedPath, fromFn, description);
    }

    static IResourceEndpointAcceptor<SuccessfulMatch> apply(String method, String templatedPath, String description) {
        return new ResourceEndpointNoFromAcceptor(method, templatedPath, description);
    }
    static <Req> IResourceEndpointAcceptor<Req> create(String method, String path, Function<ServiceRequest, Req> reqFn, String description) {
        return new ResourceEndpointFnFromAcceptor(method, path, reqFn, description);
    }

    String method();
    String templatedPath();

    default <T> Function<ServiceRequest, CompletableFuture<Optional<T>>> andIfMatches(Function<From, CompletableFuture<T>> fn) {
        return sr -> Optionals.fold(apply(sr), () -> CompletableFuture.completedFuture(Optional.empty()), from -> fn.apply(from).thenApply(t -> Optional.of(t)));
    }
}


class ResourceEndpointFnFromAcceptor<From> implements IResourceEndpointAcceptor<From> {
    final String method;
    final String path;
    final Function<ServiceRequest, From> reqFn;
    final String description;
    final String templatedPath;
    public ResourceEndpointFnFromAcceptor(String method, String templatedPath, Function<ServiceRequest, From> reqFn, String description) {
        this.method = method;
        this.templatedPath = templatedPath;
        this.path = templatedPath.replace("{host}", "");
        this.reqFn = reqFn;
        this.description = description;
    }
    @Override public String method() { return method; }
    @Override public String templatedPath() { return templatedPath; }
    @Override public Optional<From> apply(ServiceRequest serviceRequest) {
        if (method.equalsIgnoreCase(serviceRequest.method) && path.equalsIgnoreCase(serviceRequest.path))
            return Optional.of(reqFn.apply(serviceRequest));
        else
            return Optional.empty();
    }
    @Override public List<MethodPathAndDescription> description() { return List.of(new MethodPathAndDescription(method, templatedPath, description)); }
}

class ResourceWithFromEndpointAcceptor<From> implements IResourceEndpointAcceptor<From> {

    final String method;
    final String templatedPath;
    final Function<String, Optional<String>> ripper;
    final BiFunction<ServiceRequest, String, From> fromFn;
    private String description;

    public ResourceWithFromEndpointAcceptor(String method, String templatedPath, BiFunction<ServiceRequest, String, From> fromFn, String description) {
        this.method = method;
        this.templatedPath = templatedPath;
        this.ripper = Strings.ripIdFromPath(templatedPath.replace("{host}", ""));
        this.fromFn = fromFn;
        this.description = description;
    }
    @Override public String method() { return method; }
    @Override public String templatedPath() { return templatedPath; }
    @Override public Optional<From> apply(ServiceRequest serviceRequest) {
        if (!serviceRequest.method.equalsIgnoreCase(method)) return Optional.empty();
        return ripper.apply(serviceRequest.uri.getPath()).map(id -> fromFn.apply(serviceRequest, id));
    }
    @Override public List<MethodPathAndDescription> description() { return List.of(new MethodPathAndDescription(method, templatedPath, description)); }
}

class ResourceEndpointNoFromAcceptor implements IResourceEndpointAcceptor<SuccessfulMatch> {

    final String method;
    final String path;
    private String description;
    private final String originalPath;

    public ResourceEndpointNoFromAcceptor(String method, String path, String description) {
        this.method = method;
        this.originalPath = path;
        this.path = path.replace("{host}", "");
        this.description = description;
    }
    @Override public String method() { return method; }
    @Override public String templatedPath() { return path; }
    @Override public Optional<SuccessfulMatch> apply(ServiceRequest serviceRequest) {
        if (!serviceRequest.method.equalsIgnoreCase(method)) return Optional.empty();
        if (!serviceRequest.uri.getPath().equalsIgnoreCase(path)) return Optional.empty();
        return SuccessfulMatch.optMatch;
    }
    @Override public List<MethodPathAndDescription> description() { return List.of(new MethodPathAndDescription(method, originalPath, description)); }
}

