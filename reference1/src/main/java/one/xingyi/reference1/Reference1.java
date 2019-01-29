package one.xingyi.reference1;
import one.xingyi.core.endpoints.EndpointConfig;
import one.xingyi.core.marshelling.JsonValue;
import one.xingyi.core.utils.Lists;
import one.xingyi.reference1.person.PersonController;

import java.util.Objects;


public class Reference1 {
    public static void main(String[] args) {
        PersonServer<JsonValue> server = new PersonServer<>(EndpointConfig.defaultConfigNoParser, new PersonController());
        server.simpleServer(9000).start();
        System.out.println("Started backend 1: " + server.lens());
        System.out.println(Lists.mapJoin(server.endpoint().description(), "\n", Objects::toString));
    }
}
