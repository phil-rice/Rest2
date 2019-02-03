package one.xingyi.performance;
import one.xingyi.core.endpoints.EndpointConfig;
import one.xingyi.core.http.JavaHttpClient;
import one.xingyi.core.httpClient.HttpServiceCompletableFuture;
import one.xingyi.core.utils.ConsumerWithException;
import one.xingyi.reference1.PersonServer;
import one.xingyi.reference1.person.PersonController;
import one.xingyi.reference1.person.client.view.PersonLine12View;

import java.text.MessageFormat;
public class PerformanceCheck {


  public  static void main(String[] args) throws Exception {
        PersonServer personServer = new PersonServer(EndpointConfig.defaultConfigNoParser, new PersonController());
        personServer.simpleServer(9000).start();
        HttpServiceCompletableFuture service = HttpServiceCompletableFuture.defaultService("http://localhost:9000", JavaHttpClient.client);
        PersonLine12View.create(service, "someIdToWakeTheSystemUp").get();
        PersonLine12View.get(service, "someIdToWakeTheSystemUp", i -> i.line1()).get();

        doOperation(service, "Created {0} in {1} ms", (i) -> PersonLine12View.create(service, i).get());
        doOperation(service, "Created {0} in {1} ms", (i) -> PersonLine12View.get(service, i, l -> l.line1()).get());
        doOperation(service, "Created {0} in {1} ms", (i) -> PersonLine12View.get(service, i, l -> l.line1()).get());
    }
    private static void doOperation(HttpServiceCompletableFuture service, String pattern, ConsumerWithException<String> consumer) throws Exception {
        long startTime = System.nanoTime();
        int size = 100;
        for (int i = 0; i < size; i++)
            consumer.accept(Integer.toString(i));
        System.out.println(MessageFormat.format(pattern, size, (System.nanoTime() - startTime) / 1000000));
    }
}
