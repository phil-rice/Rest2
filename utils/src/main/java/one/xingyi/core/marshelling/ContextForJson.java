package one.xingyi.core.marshelling;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.http.ServiceRequest;

public interface ContextForJson {
    String protocol();

    String template(String raw);
    static ContextForJson nullContext = new NullContext();
    static ContextForJson forServiceRequest(String protocol, ServiceRequest serviceRequest) { return new ServiceRequestContextForJson(protocol, serviceRequest);}
}

class NullContext implements ContextForJson {

    @Override public String protocol() {
        return "";
    }
    @Override public String template(String raw) { return raw.replaceAll("<host>", ""); }
}

@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
class ServiceRequestContextForJson implements ContextForJson {
    final String protocol;
    final ServiceRequest serviceRequest;
    public String protocol() { return protocol;}
    @Override public String template(String raw) {
        return raw.replaceAll("<host>", serviceRequest.header("host").map(s -> protocol + s).orElse(""));
    }
}