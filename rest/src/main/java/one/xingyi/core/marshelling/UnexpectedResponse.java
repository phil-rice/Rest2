package one.xingyi.core.marshelling;
import one.xingyi.core.http.ServiceResponse;
public class UnexpectedResponse extends RuntimeException {
    public final ServiceResponse response;
    public UnexpectedResponse(ServiceResponse response) {
        super(response.toString());
        this.response = response;
    }
    public UnexpectedResponse(String message, ServiceResponse response) {
        super(message + response);
        this.response = response;
    }
}