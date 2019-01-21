package one.xingyi.core.endpoints;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.utils.Optionals;

import java.util.Optional;
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
    private final String startString;
    private final String endString;
    private final BiFunction<ServiceRequest,String, From> fn;

    public BookmarkAcceptor(String method, String bookmark, BiFunction<ServiceRequest,String, From> fn) {
        this.method = method;
        int index = bookmark.indexOf("<id>");
        this.fn = fn;
        if (index == -1) throw new IllegalArgumentException("Bookmark: " + bookmark + " is invalid");
        startString = bookmark.substring(0, index);
        endString = bookmark.substring(index + 4);
    }
    @Override public Optional<From> apply(ServiceRequest serviceRequest) {
        if (!serviceRequest.method.equalsIgnoreCase(method)) return Optional.empty();
        String url = serviceRequest.url.getPath();
        if (url.startsWith(startString) && url.endsWith(endString)) {
            String substring = url.substring(startString.length(), url.length() - endString.length());
            if (substring.indexOf("/") != -1)return Optional.empty();
            return Optional.of(fn.apply(serviceRequest, substring));
        }
        return Optional.empty();
    }
}