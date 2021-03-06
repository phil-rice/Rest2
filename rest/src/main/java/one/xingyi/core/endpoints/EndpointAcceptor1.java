package one.xingyi.core.endpoints;
import one.xingyi.core.http.ServiceRequest;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
public interface EndpointAcceptor1<From> extends Function<ServiceRequest, Optional<From>>, MethodAndPathDescription {

    List<MethodPathAndDescription> description();
//    static <From> EndpointAcceptor1<From> justOneThing(String method, Function<String, From> fn) { return new JustOneThing<>(method, fn); }
//    static <From> EndpointAcceptor1<From> nameThenId(String method, String name, Function<String, From> fn) { return new NameThenid<>(method, name, fn); }
//    static <From> EndpointAcceptor1<From> bookmarkAcceptor(String method, String bookmakr, BiFunction<ServiceRequest, String, From> fn) { return new BookmarkAcceptor<>(method, bookmakr, fn); }

}

//@RequiredArgsConstructor
//@ToString
//class JustOneThing<From> implements EndpointAcceptor1<From> {
//    final String method;
//    final Function<String, From> fn;
//
//    @Override public Optional<From> apply(ServiceRequest sr) {
//        return Optionals.from(sr.segmentsCount() == 2 && method.equalsIgnoreCase(sr.method), () -> fn.apply(sr.lastSegment()));
//    }
//    @Override public List<MethodPathAndDescription> description() {
//        return List.of(new MethodPathAndDescription(method, "/{anyonesegment}"));
//    }
//}
//@RequiredArgsConstructor
//@ToString
//class NameThenid<From> implements EndpointAcceptor1<From> {
//    final String method;
//    final String name;
//    final Function<String, From> fn;
//
//    @Override public Optional<From> apply(ServiceRequest sr) {
//        return Optionals.from(sr.segmentsCount() == 3 && name.equalsIgnoreCase(sr.urlSegments().get(1)) && method.equalsIgnoreCase(sr.method), () -> fn.apply(sr.lastSegment()));
//    }
//    @Override public List<MethodPathAndDescription> description() {
//        return List.of(new MethodPathAndDescription(method, "/" + name + "/{id}"));
//    }
//}
//
//@RequiredArgsConstructor
//@ToString
//class BookmarkAcceptor<From> implements EndpointAcceptor1<From> {
//    private final String method;
//    private final Function<String, Optional<String>> ripper;
//    private String bookmark;
//    private final BiFunction<ServiceRequest, String, From> fn;
//
//    public BookmarkAcceptor(String method, String rawBookmark, BiFunction<ServiceRequest, String, From> fn) {
//        this.method = method;
//        bookmark = rawBookmark.replace("{host}", "");
//        this.ripper = Strings.ripIdFromPath(bookmark);
//        this.fn = fn;
//    }
//
//    @Override public Optional<From> apply(ServiceRequest serviceRequest) {
//        if (!serviceRequest.method.equalsIgnoreCase(method)) return Optional.empty();
//        String url = serviceRequest.uri.getPath();
//        return ripper.apply(serviceRequest.uri.getPath()).map(id -> fn.apply(serviceRequest, id));
//    }
//    @Override public List<MethodPathAndDescription> description() {
//        return List.of(new MethodPathAndDescription(method, bookmark));
//    }
//}