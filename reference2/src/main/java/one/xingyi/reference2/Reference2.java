package one.xingyi.reference2;
import one.xingyi.core.endpoints.EndpointConfig;
import one.xingyi.core.marshelling.JsonValue;
import one.xingyi.core.server.EndpointHandler;
import one.xingyi.core.server.HttpUtils;
import one.xingyi.core.server.SimpleServer;
import one.xingyi.core.utils.Lists;
import one.xingyi.json.Json;
import one.xingyi.reference2.person.PersonController;

import java.util.List;
import java.util.Objects;


public class Reference2 {
    public static void main(String[] args) {
        PersonServer<Object> server = new PersonServer<>(EndpointConfig.defaultConfig(new Json()), new PersonController());
        new SimpleServer(HttpUtils.makeDefaultExecutor(), new EndpointHandler(server.endpoint()), 9000).start();
        System.out.println("Started backend 1: " + server.lens());
        System.out.println(Lists.mapJoin(server.endpoint().description(), "\n", Objects::toString));
        System.out.println();
        System.out.println(Lists.join(server.context.javascriptStore.find(List.of()), "\n"));
    }
}
