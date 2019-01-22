package one.xingyi.core.endpoints;
public interface EndPointFactory {
    <J> EndPoint create(EndpointContext<J> context);
 }

