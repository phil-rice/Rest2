package one.xingyi.core.sdk;
import one.xingyi.core.endpoints.EndPoint;
import one.xingyi.core.reflection.ReflectionOn;

import java.util.List;
public interface IXingYiServer {


    default List<EndPoint> allEndpoints() { return new ReflectionOn<>(this).methodsWithReturnType(EndPoint.class); }

}
