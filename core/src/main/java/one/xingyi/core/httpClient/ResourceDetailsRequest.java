package one.xingyi.core.httpClient;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.endpoints.EndpointRequest;
import one.xingyi.core.http.ServiceRequest;

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class ResourceDetailsRequest implements EndpointRequest {
   public static ResourceDetailsRequest create(ServiceRequest serviceRequest, String id){ return new ResourceDetailsRequest(serviceRequest.path); }
   public  final String entityName;
}
