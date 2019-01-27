package one.xingyi.core.httpClient;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.endpoints.EndpointRequest;
import one.xingyi.core.http.ServiceRequest;

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class EntityDetailsRequest implements EndpointRequest {
   public static EntityDetailsRequest create(ServiceRequest serviceRequest, String id){ return new EntityDetailsRequest(serviceRequest.path); }
   public  final String entityName;
}
