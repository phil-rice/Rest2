package one.xingyi.core.endpoints;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.utils.Optionals;

import java.util.Optional;
import java.util.function.Function;
public interface EndpointAcceptor1<From> extends Function<ServiceRequest, Optional<From>> {

    static <From> EndpointAcceptor1<From> justOneThing(String method, Function<String, From> fn) { return new JustOneThing<>(method, fn); }
    static <From> EndpointAcceptor1<From> nameThenId(String method, String name, Function<String, From> fn) { return new NameThenid<>(method, name, fn); }
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

