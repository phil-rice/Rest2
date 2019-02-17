package one.xingyi.core.server;
import com.sun.net.httpserver.HttpExchange;
import lombok.val;
import one.xingyi.core.http.Header;
import one.xingyi.core.http.ServiceResponse;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
public class HttpUtils {

    public static ExecutorService makeDefaultExecutor() {
        return Executors.newFixedThreadPool(100, new ThreadFactory() {
            public Thread newThread(Runnable r) {
                Thread t = Executors.defaultThreadFactory().newThread(r);
                t.setDaemon(true);
                return t;
            }
        });
    }
    public static void write(HttpExchange exchange, ServiceResponse response) throws IOException {
        for (Header h : response.headers)
            exchange.getResponseHeaders().set(h.name, h.value);
        //    exchange.getResponseHeaders.set("content-type", response.contentType.fold("text/plain")(_.value))
        val bytes = response.body.getBytes("UTF-8");
        exchange.sendResponseHeaders(response.statusCode, bytes.length);
        Streams.sendAll(exchange.getResponseBody(), bytes);
    }

}
