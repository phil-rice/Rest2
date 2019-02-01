package one.xingyi.reference1;
import one.xingyi.core.endpoints.EndpointConfig;
import one.xingyi.core.marshelling.JsonValue;
import one.xingyi.core.server.EndpointHandler;
import one.xingyi.core.server.HttpUtils;
import one.xingyi.core.server.SimpleServer;
import one.xingyi.core.utils.Lists;
import one.xingyi.reference1.person.PersonController;

import java.util.Objects;


public class Reference1 {
    public static void main(String[] args) {
        PersonServer<JsonValue> server = new PersonServer<>(EndpointConfig.defaultConfigNoParser, new PersonController());
        new SimpleServer(HttpUtils.makeDefaultExecutor(), new EndpointHandler(server.endpoint()), 9000).start();

        System.out.println("Started backend 1: " + server.lens());
        System.out.println(Lists.mapJoin(server.endpoint().description(), "\n", Objects::toString));
    }
}
