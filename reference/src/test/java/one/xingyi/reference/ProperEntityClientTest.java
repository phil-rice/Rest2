package one.xingyi.reference;
import one.xingyi.core.http.JavaHttpClient;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.http.ServiceResponse;
import one.xingyi.core.server.EndpointHandler;
import one.xingyi.core.server.SimpleServer;
import org.junit.AfterClass;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
public class ProperEntityClientTest extends AbstractEntityClientTest {

    private static SimpleServer server;
    private static ExecutorService executorService;

    @AfterClass
    public static void killServer() {
        server.stop();
        executorService.shutdownNow();
    }

    @Override protected Function<ServiceRequest, CompletableFuture<ServiceResponse>> httpClient() {
        if (server == null) {
            executorService = Executors.newFixedThreadPool(20);
            server = new SimpleServer(executorService, new EndpointHandler(entityEndpoints()), 9000);
            server.start();
        }
        return JavaHttpClient.client;
    }
    @Override protected String expectedHost() { return "http://localhost:9000"; }
}
