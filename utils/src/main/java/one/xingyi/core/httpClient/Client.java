package one.xingyi.core.httpClient;
import one.xingyi.core.sdk.IXingYiView;

import java.net.URLEncoder;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
public interface Client {

    <Interface extends IXingYiView<?>, Result> CompletableFuture<Result> primitiveGet(Class<Interface> interfaceClass, String url, Function<Interface, Result> fn);

    <Interface extends IXingYiView<?>> CompletableFuture<String> getUrlPattern(Class<Interface> interfaceClass);

    default <Interface extends IXingYiView<?>, Result> CompletableFuture<Result> get(Class<Interface> interfaceClass, String id, Function<Interface, Result> fn) {
        return getUrlPattern(interfaceClass).thenCompose(url -> primitiveGet(interfaceClass, url.replace("<id>", URLEncoder.encode(id)), fn));
    }
}



