package one.xingyi.core.marshelling;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.http.ServiceRequest;

public interface ContextForJson {
    String protocolAndHost();

    static ContextForJson nullContext = new NullContext();
    static ContextForJson forServiceRequest(ServiceRequest serviceRequest) { return new ServiceRequestContextForJson(serviceRequest);}
}

class NullContext implements ContextForJson {

    @Override public String protocolAndHost() {
        return "<hostAndDomain>";
    }
}

@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
class ServiceRequestContextForJson implements ContextForJson {
    final ServiceRequest serviceRequest;
    public String protocolAndHost() { return serviceRequest.header("host").map(h -> "http://" + h).orElse("");}
}