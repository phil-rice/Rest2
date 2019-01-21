package one.xingyi.core.httpClient;
import lombok.RequiredArgsConstructor;
import one.xingyi.core.client.IXingYi;
import one.xingyi.core.client.IXingYiFactory;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.http.ServiceResponse;
import one.xingyi.core.marshelling.DataAndJavaScript;
import one.xingyi.core.marshelling.IXingYiResponseSplitter;
import one.xingyi.core.sdk.IXingYiClientEntity;
import one.xingyi.core.sdk.IXingYiClientMaker;
import one.xingyi.core.sdk.IXingYiView;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
public interface HttpService {

public     static HttpService defaultService(Function<ServiceRequest, CompletableFuture<ServiceResponse>> delegate) {
        return new DefaultHttpService(delegate, IXingYiFactory.simple, IXingYiResponseSplitter.splitter);
    }

    <Entity extends IXingYiClientEntity, View extends IXingYiView<Entity>, Result> CompletableFuture<Result> primitiveGet(
            IXingYiClientMaker<Entity, View> clientMaker,
            String url,
            Function<View, Result> fn);
    <Entity extends IXingYiClientEntity, View extends IXingYiView<Entity>, Result> CompletableFuture<Result> get(
            IXingYiClientMaker<Entity, View> clientMaker,
            String id,
            Function<View, Result> fn);

//    <Interface extends IXingYiView<?>> CompletableFuture<String> getUrlPattern(Class<Interface> interfaceClass);
//
//    default <Interface extends IXingYiView<?>, Result> CompletableFuture<Result> get(Class<Interface> interfaceClass, String id, Function<Interface, Result> fn) {
//        return getUrlPattern(interfaceClass).thenCompose(url -> primitiveGet(interfaceClass, url.replace("<id>", URLEncoder.encode(id)), fn));
//    }
}

@RequiredArgsConstructor
class DefaultHttpService implements HttpService {
    final Function<ServiceRequest, CompletableFuture<ServiceResponse>> service;
    final IXingYiFactory factory;
    final IXingYiResponseSplitter splitter;

    @Override public <Entity extends IXingYiClientEntity, View extends IXingYiView<Entity>, Result> CompletableFuture<Result>
    primitiveGet(IXingYiClientMaker<Entity, View> clientMaker, String url, Function<View, Result> fn) {
        ServiceRequest serviceRequest = new ServiceRequest("get", url, List.of(), "");
        return service.apply(serviceRequest).thenApply(serviceResponse -> {
            DataAndJavaScript dataAndJavaScript = splitter.apply(serviceResponse);
            IXingYi<Entity, View> xingYi = factory.apply(dataAndJavaScript.javascript);
            Object mirror = xingYi.parse(dataAndJavaScript.data);
            return fn.apply(clientMaker.create(xingYi, mirror));
        });
    }
    @Override public <Entity extends IXingYiClientEntity, View extends IXingYiView<Entity>, Result> CompletableFuture<Result> get(IXingYiClientMaker<Entity, View> clientMaker, String id, Function<View, Result> fn) {
        return null;
    }
}


