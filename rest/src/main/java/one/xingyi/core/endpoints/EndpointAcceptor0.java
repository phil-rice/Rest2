package one.xingyi.core.endpoints;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import one.xingyi.core.http.ServiceRequest;

import java.util.List;
import java.util.function.Function;
public interface EndpointAcceptor0 extends Function<ServiceRequest, Boolean>, MethodAndPathDescription{

    List<MethodAndPath> description();

    static EndpointAcceptor0 exact(String method, String path) {
        return new ExactAcceptor0(method, path);
    }
}


@EqualsAndHashCode
@ToString
class ExactAcceptor0 implements EndpointAcceptor0 {
    final String method;
    final String templatedPath;
     final String path;
    public ExactAcceptor0(String method, String templatedPath) {
        this.method = method;
        this.templatedPath = templatedPath;
        this.path = templatedPath.replace("{host}", "");
    }
    @Override public Boolean apply(ServiceRequest sr) {
        return sr.method.equalsIgnoreCase(method) && sr.uri.getPath().equalsIgnoreCase(path);
    }
    @Override public List<MethodAndPath> description() {
        return List.of(new MethodAndPath(method, templatedPath));
    }
}
