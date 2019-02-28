package one.xingyi.core.server;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import one.xingyi.core.endpoints.EndPoint;
import one.xingyi.core.utils.ConsumerWithException;
import one.xingyi.core.utils.WrappedException;

import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

@ToString
@EqualsAndHashCode
//@RequiredArgsConstructor
public class SimpleServer {

    final HttpServer server;
    final Executor executor;
    final int port;

    public static void doAndThenStop(int port, EndPoint endPoints, ConsumerWithException<SimpleServer> consumer) {
        ExecutorService executor = HttpUtils.makeDefaultExecutor();
        SimpleServer server = new SimpleServer(executor, new EndpointHandler(endPoints), port);
        try {
            server.start();
            WrappedException.wrapConsumer(consumer).accept(server);
        } finally {
            server.stop();
            executor.shutdown();
        }
    }


    public SimpleServer(Executor executor, HttpHandler handler, int port) {
        this.server = WrappedException.wrapCallable(() -> HttpServer.create());
        this.executor = executor;
        this.port = port;
        server.createContext("/", handler);
        server.setExecutor(executor);
    }

    public void start() {
        WrappedException.wrap(() -> server.bind(new InetSocketAddress(port), 0));
        server.start();
    }
    public void stop() {
        server.stop(1);
    }
    public void stopAndKillExecutors() {
        server.stop(1);
    }

}
