package one.xingyi.core.endpoints;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.utils.Optionals;
import one.xingyi.core.utils.Strings;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Function;
public interface EndpointAcceptor1<From> extends Function<ServiceRequest, Optional<From>> {

    static <From> EndpointAcceptor1<From> justOneThing(String method, Function<String, From> fn) { return new JustOneThing<>(method, fn); }
    static <From> EndpointAcceptor1<From> nameThenId(String method, String name, Function<String, From> fn) { return new NameThenid<>(method, name, fn); }
    static <From> EndpointAcceptor1<From> bookmarkAcceptor(String method, String bookmakr, BiFunction<ServiceRequest, String, From> fn) { return new BookmarkAcceptor<>(method, bookmakr, fn); }

}

@RequiredArgsConstructor
@ToString
class JustOneThing<From> implements EndpointAcceptor1<From> {
    final String method;
    final Function<String, From> fn;

    @Override public Optional<From> apply(ServiceRequest sr) {
        return Optionals.from(sr.segmentsCount() == 2 && method.equalsIgnoreCase(sr.method), () -> fn.apply(sr.lastSegment()));
    }
}
@RequiredArgsConstructor
@ToString
class NameThenid<From> implements EndpointAcceptor1<From> {
    final String method;
    final String name;
    final Function<String, From> fn;

    @Override public Optional<From> apply(ServiceRequest sr) {
        return Optionals.from(sr.segmentsCount() == 3 && name.equalsIgnoreCase(sr.urlSegments().get(1)) && method.equalsIgnoreCase(sr.method), () -> fn.apply(sr.lastSegment()));
    }
}

@RequiredArgsConstructor
@ToString
class BookmarkAcceptor<From> implements EndpointAcceptor1<From> {
    private final String method;
    private final Function<String, Optional<String>> ripper;
    private final BiFunction<ServiceRequest, String, From> fn;

    public BookmarkAcceptor(String method, String rawBookmark, BiFunction<ServiceRequest, String, From> fn) {
        this.method = method;
        this.ripper = Strings.ripIdFromPath(rawBookmark.replace("{host}", ""));
        this.fn = fn;
    }

    @Override public Optional<From> apply(ServiceRequest serviceRequest) {
        if (!serviceRequest.method.equalsIgnoreCase(method)) return Optional.empty();
        String url = serviceRequest.url.getPath();
        return ripper.apply(serviceRequest.url.getPath()).map(id -> fn.apply(serviceRequest, id));
    }
}