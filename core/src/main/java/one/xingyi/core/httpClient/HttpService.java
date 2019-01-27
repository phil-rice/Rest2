package one.xingyi.core.httpClient;
import lombok.RequiredArgsConstructor;
import one.xingyi.core.client.IXingYi;
import one.xingyi.core.client.IXingYiFactory;
import one.xingyi.core.http.JavaHttpClient;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.http.ServiceResponse;
import one.xingyi.core.httpClient.client.companion.UrlPatternCompanion;
import one.xingyi.core.httpClient.client.view.UrlPattern;
import one.xingyi.core.marshelling.DataAndJavaScript;
import one.xingyi.core.marshelling.IXingYiResponseSplitter;
import one.xingyi.core.sdk.IXingYiClientEntity;
import one.xingyi.core.sdk.IXingYiClientMaker;
import one.xingyi.core.sdk.IXingYiRemoteAccessDetails;
import one.xingyi.core.sdk.IXingYiView;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
public interface HttpService {

    static HttpService defaultService(String protocolAndHost, Function<ServiceRequest, CompletableFuture<ServiceResponse>> delegate) {
        return new DefaultHttpService(protocolAndHost, delegate, IXingYiFactory.simple, IXingYiResponseSplitter.splitter);
    }
    static HttpService defaultServiceWithSimpleJavaclint(String protocolAndHost) {return defaultService(protocolAndHost, JavaHttpClient.client); }

    <Entity extends IXingYiClientEntity, View extends IXingYiView<Entity>, Result> CompletableFuture<Result> primitiveGet(
            IXingYiRemoteAccessDetails<Entity, View> clientMaker,
            String url,
            Function<View, Result> fn);

    default CompletableFuture<String> getUrlPattern(String bookmark) {
        return UrlPatternCompanion.companion.getPrimitive(this, bookmark, UrlPattern::urlPattern);
    }

    <Entity extends IXingYiClientEntity, View extends IXingYiView<Entity>, Result> CompletableFuture<Result> get(
            IXingYiRemoteAccessDetails<Entity, View> clientMaker,
            String id,
            Function<View, Result> fn);

//    <Interface extends IXingYiView<?>> CompletableFuture<String> getUrlPattern(Class<Interface> interfaceClass);
//
//    default <Interface extends IXingYiView<?>, Result> CompletableFuture<Result> get(Class<Interface> interfaceClass, String id, Function<Interface, Result> fn) {
//        return getUrlPattern(interfaceClass).thenCompose(url -> primitiveGet(interfaceClass, url.replace("{id}", URLEncoder.encode(id)), fn));
//    }
}

@RequiredArgsConstructor
class DefaultHttpService implements HttpService {
    final String protocolAndHost;
    final Function<ServiceRequest, CompletableFuture<ServiceResponse>> service;
    final IXingYiFactory factory;
    final IXingYiResponseSplitter splitter;

    @Override public <Entity extends IXingYiClientEntity, View extends IXingYiView<Entity>, Result> CompletableFuture<Result> primitiveGet(IXingYiRemoteAccessDetails<Entity, View> clientMaker, String url, Function<View, Result> fn) {
        String fullUrl = url.startsWith("/") ? protocolAndHost + url : url;
        ServiceRequest serviceRequest = new ServiceRequest("get", fullUrl, List.of(), "");
        return service.apply(serviceRequest).thenApply(serviceResponse -> {
            try {
                DataAndJavaScript dataAndJavaScript = splitter.apply(serviceResponse);
                IXingYi<Entity, View> xingYi = factory.apply(dataAndJavaScript.javascript);
                Object mirror = xingYi.parse(dataAndJavaScript.data);
                return fn.apply(clientMaker.create(xingYi, mirror));
            } catch (Exception e) {
                throw new RuntimeException("Have thrown unexpected exception.\n" + serviceRequest + "\n" + serviceResponse, e);
            }
        });
    }
    @Override public <Entity extends IXingYiClientEntity, View extends IXingYiView<Entity>, Result> CompletableFuture<Result> get(IXingYiRemoteAccessDetails<Entity, View> clientMaker, String id, Function<View, Result> fn) {
        return getUrlPattern(clientMaker.bookmark()).thenCompose(urlPattern -> {
            return primitiveGet(clientMaker, urlPattern.replace("{id}", id), fn);//TODO UrlEncoding
        });

    }
}


