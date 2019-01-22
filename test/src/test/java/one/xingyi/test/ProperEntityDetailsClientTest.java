package one.xingyi.test;
import one.xingyi.core.http.JavaHttpClient;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.http.ServiceResponse;
import one.xingyi.core.server.EndpointHandler;
import one.xingyi.core.server.SimpleServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
public class ProperEntityDetailsClientTest extends AbstractEntityDetailsClientTest {

    private static SimpleServer server;
    private static ExecutorService executorService;

    @AfterClass
    public static void killServer() {
        server.stop();
        executorService.shutdownNow();
    }

    @BeforeClass
    public static void startServer() {
        executorService = Executors.newFixedThreadPool(20);
        server = new SimpleServer(executorService, new EndpointHandler(entityEndpoints), 9000);
        server.start();

    }

    @Override protected Function<ServiceRequest, CompletableFuture<ServiceResponse>> httpClient() { return JavaHttpClient.client; }
    @Override protected String expectedHost() { return "http://localhost:9000"; }
}
