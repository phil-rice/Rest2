package one.xingyi.core.endpoints;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import one.xingyi.core.http.ServiceRequest;

import java.util.List;
import java.util.function.Function;
public interface EndpointAcceptor0 extends Function<ServiceRequest, Boolean>, MethodAndPathDescription {

    List<MethodPathAndDescription> description();

    static EndpointAcceptor0 exact(String method, String path, String description) {
        return new ExactAcceptor0(method, path, description);
    }
}


@EqualsAndHashCode
@ToString
class ExactAcceptor0 implements EndpointAcceptor0 {
    final String method;
    final String templatedPath;
    final String path;
    private String description;
    public ExactAcceptor0(String method, String templatedPath, String description) {
        this.method = method;
        this.templatedPath = templatedPath;
        this.path = templatedPath.replace("{host}", "");
        this.description = description;
    }
    @Override public Boolean apply(ServiceRequest sr) {
        return sr.method.equalsIgnoreCase(method) && sr.uri.getPath().equalsIgnoreCase(path);
    }
    @Override public List<MethodPathAndDescription> description() {
        return List.of(new MethodPathAndDescription(method, templatedPath, description));
    }
}
