package one.xingyi.core.httpClient;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.endpoints.EndpointRequest;

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class EntityDetailsRequest implements EndpointRequest {
   public  final String entityName;
}
