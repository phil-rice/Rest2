package one.xingyi.performance;
import one.xingyi.core.endpoints.EndpointConfig;
import one.xingyi.core.http.JavaHttpClient;
import one.xingyi.core.httpClient.HttpServiceCompletableFuture;
import one.xingyi.core.server.SimpleServer;
import one.xingyi.core.utils.ConsumerWithException;
import one.xingyi.json.Json;
import one.xingyi.reference1.PersonServer;
import one.xingyi.reference1.person.PersonController;
import one.xingyi.reference1.person.client.view.PersonLine12View;

import java.text.MessageFormat;
public class PerformanceCheck {


    public static void main(String[] args) throws Exception {
        PersonServer personServer = new PersonServer(EndpointConfig.defaultConfig(new Json()), new PersonController());
        SimpleServer simpleServer = personServer.simpleServer(9000);
        simpleServer.start();
        HttpServiceCompletableFuture service = HttpServiceCompletableFuture.lensService("http://localhost:9000", new Json(), JavaHttpClient.client);
        PersonLine12View.create(service, "someIdToWakeTheSystemUp").get();
        PersonLine12View.get(service, "someIdToWakeTheSystemUp", i -> i.line1()).get();

        int createdSize = 1000;
        doOperation(service, createdSize, "Created {0} in {1} ms", (i) -> PersonLine12View.create(service, i.toString()).get());
        doOperation(service, createdSize * 1, "get {0} in {1} ms", (i) -> PersonLine12View.get(service, Integer.toString(i % createdSize), l -> l.line1()).get());
        doOperation(service, createdSize * 1, "get {0} in {1} ms", (i) -> PersonLine12View.get(service, Integer.toString(i % createdSize), l -> l.line1()).get());
        doOperation(service, createdSize * 10, "get {0} in {1} ms", (i) -> PersonLine12View.get(service, Integer.toString(i % createdSize), l -> l.line1()).get());
        doOperation(service, createdSize * 10, "get {0} in {1} ms", (i) -> PersonLine12View.get(service, Integer.toString(i % createdSize), l -> l.line1()).get());
        doOperation(service, createdSize * 10, "get {0} in {1} ms", (i) -> PersonLine12View.get(service, Integer.toString(i % createdSize), l -> l.line1()).get());
        doOperation(service, createdSize * 10, "get {0} in {1} ms", (i) -> PersonLine12View.get(service, Integer.toString(i % createdSize), l -> l.line1()).get());
        doOperation(service, createdSize * 100, "get {0} in {1} ms", (i) -> PersonLine12View.get(service, Integer.toString(i % createdSize), l -> l.line1()).get());
        simpleServer.stop();
    }
    private static void doOperation(HttpServiceCompletableFuture service, int size, String pattern, ConsumerWithException<Integer> consumer) throws Exception {
        long startTime = System.nanoTime();
        for (int i = 0; i < size; i++)
            consumer.accept(i);
        System.out.println(MessageFormat.format(pattern, size, (System.nanoTime() - startTime) / 1000000));
    }
}
