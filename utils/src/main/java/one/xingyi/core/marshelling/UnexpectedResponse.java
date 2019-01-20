package one.xingyi.core.marshelling;
import one.xingyi.core.http.ServiceResponse;
public class UnexpectedResponse extends RuntimeException {
    public UnexpectedResponse(ServiceResponse response) {
        super(response.toString());
    }
    public UnexpectedResponse(String message, ServiceResponse response) {
        super(message + response);
    }
}