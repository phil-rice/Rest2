package one.xingyi.core.endpoints;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.http.ServiceRequest;

import java.util.function.Function;
public interface EndpointAcceptor0 extends Function<ServiceRequest, Boolean> {

    static <From> EndpointAcceptor0 exact(String method, String path) {
        return sr -> sr.method.equalsIgnoreCase(method) && sr.url.getPath().equalsIgnoreCase(path);
    }
}

@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
class ExactAcceptor0 implements EndpointAcceptor0{
    final String method;
    final String path;
    @Override public Boolean apply(ServiceRequest sr) {
        return sr.method.equalsIgnoreCase(method) && sr.url.getPath().equalsIgnoreCase(path);
    }
}
