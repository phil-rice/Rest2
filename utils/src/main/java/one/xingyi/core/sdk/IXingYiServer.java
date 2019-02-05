package one.xingyi.core.sdk;
import one.xingyi.core.endpoints.EndPoint;
import one.xingyi.core.optics.lensLanguage.LensLine;
import one.xingyi.core.reflection.ReflectionOn;
import one.xingyi.core.server.EndpointHandler;
import one.xingyi.core.server.HttpUtils;
import one.xingyi.core.server.SimpleServer;
import one.xingyi.core.utils.Lists;

import java.util.List;
import java.util.Set;
public interface IXingYiServer {
    List<IXingYiServerCompanion<?, ?>> companions();


    default List<EndPoint> allEndpoints() { return new ReflectionOn<>(this).methodsWithReturnType(EndPoint.class, m -> !m.getName().equalsIgnoreCase("endpoint")); }
    default EndPoint endpoint() { return EndPoint.compose(allEndpoints());}
    default SimpleServer simpleServer(int port) {return new SimpleServer(HttpUtils.makeDefaultExecutor(), new EndpointHandler(endpoint()), port);}
    default SimpleServer simpleServerWithLog(int port) {return new SimpleServer(HttpUtils.makeDefaultExecutor(), new EndpointHandler(EndPoint.printlnLog(endpoint())), port);}
    default List<LensLine> allLensLens() {return Lists.flatMap(companions(), IXingYiServerCompanion::lensLines);}
}
