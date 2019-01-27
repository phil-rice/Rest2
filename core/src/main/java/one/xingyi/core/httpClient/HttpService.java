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
import one.xingyi.core.sdk.IXingYiRemoteAccessDetails;
import one.xingyi.core.sdk.IXingYiView;
import one.xingyi.core.utils.IdAndValue;
import org.checkerframework.checker.nullness.Opt;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Function;
//TODO This whole class needs sorting out. It is far too big and does too much
public interface HttpService {

    static HttpService defaultService(String protocolAndHost, Function<ServiceRequest, CompletableFuture<ServiceResponse>> delegate) {
        return new DefaultHttpService(protocolAndHost, delegate, IXingYiFactory.simple, IXingYiResponseSplitter.splitter);
    }
    static HttpService defaultServiceWithSimpleJavaclint(String protocolAndHost) {return defaultService(protocolAndHost, JavaHttpClient.client); }

    <Entity extends IXingYiClientEntity, View extends IXingYiView<Entity>, Result> CompletableFuture<Result> primitive(
            IXingYiRemoteAccessDetails<Entity, View> clientMaker,
            String method,
            String url,
            Function<View, Result> fn);

    public <Entity extends IXingYiClientEntity, View extends IXingYiView<Entity>, Result> CompletableFuture<Boolean> primitiveForBoolean(String method, String url);
    <Entity extends IXingYiClientEntity, View extends IXingYiView<Entity>, Result> CompletableFuture<IdAndValue<View>> primitiveForIdAndValue(IXingYiRemoteAccessDetails<Entity, View> clientMaker, String method, String url);
    default CompletableFuture<String> getUrlPattern(String bookmark) {
        return UrlPatternCompanion.companion.primitive(this, "get", bookmark, UrlPattern::urlPattern);
    }

    <Entity extends IXingYiClientEntity, View extends IXingYiView<Entity>, Result> CompletableFuture<Result> get(
            IXingYiRemoteAccessDetails<Entity, View> clientMaker,
            String id,
            Function<View, Result> fn);
    public <Entity extends IXingYiClientEntity, View extends IXingYiView<Entity>, Result> CompletableFuture<Optional<Result>> primitiveForOptional(
            IXingYiRemoteAccessDetails<Entity, View> clientMaker, String method, String url, Function<View, Result> fn);

    <Entity extends IXingYiClientEntity, View extends IXingYiView<Entity>, Result> CompletableFuture<Optional<Result>> getOptional(
            IXingYiRemoteAccessDetails<Entity, View> clientMaker,
            String id,
            Function<View, Result> fn);
    <Entity extends IXingYiClientEntity, View extends IXingYiView<Entity>> CompletableFuture<View> create(
            IXingYiRemoteAccessDetails<Entity, View> clientMaker,
            String id);
    <Entity extends IXingYiClientEntity, View extends IXingYiView<Entity>> CompletableFuture<IdAndValue<View>> createWithoutId(IXingYiRemoteAccessDetails<Entity, View> clientMaker);
    <Entity extends IXingYiClientEntity, View extends IXingYiView<Entity>> CompletableFuture<Boolean> delete(IXingYiRemoteAccessDetails<Entity, View> clientMaker, String id);
    <Entity extends IXingYiClientEntity, View extends IXingYiView<Entity>> CompletableFuture<View> edit(IXingYiRemoteAccessDetails<Entity, View> clientMaker, String id, Function<View, View> fn);

}


@RequiredArgsConstructor
class DefaultHttpService implements HttpService {
    final String protocolAndHost;
    final Function<ServiceRequest, CompletableFuture<ServiceResponse>> service;
    final IXingYiFactory factory;
    final IXingYiResponseSplitter splitter;


    <Entity extends IXingYiClientEntity, View extends IXingYiView<Entity>> Function<ServiceResponse, View> makeEntity(IXingYiRemoteAccessDetails<Entity, View> clientMaker, ServiceRequest serviceRequest) {
        return serviceResponse -> {//TODO extract the try catch
            try {
                DataAndJavaScript dataAndJavaScript = splitter.apply(serviceResponse);
                IXingYi<Entity, View> xingYi = factory.apply(dataAndJavaScript.javascript);
                Object mirror = xingYi.parse(dataAndJavaScript.data);
                return clientMaker.make(xingYi, mirror);
            } catch (Exception e) {
                throw new RuntimeException("Have thrown unexpected exception.\n" + serviceRequest + "\n" + serviceResponse, e);
            }
        };
    }
    <Entity extends IXingYiClientEntity, View extends IXingYiView<Entity>> Function<ServiceResponse, Optional<View>> makeOptionalEntity(IXingYiRemoteAccessDetails<Entity, View> clientMaker, ServiceRequest serviceRequest) {
        return serviceResponse -> {
            try {
                if (serviceResponse.statusCode == 404) return Optional.empty();
                DataAndJavaScript dataAndJavaScript = splitter.apply(serviceResponse);
                IXingYi<Entity, View> xingYi = factory.apply(dataAndJavaScript.javascript);
                Object mirror = xingYi.parse(dataAndJavaScript.data);
                return Optional.of(clientMaker.make(xingYi, mirror));
            } catch (Exception e) {
                throw new RuntimeException("Have thrown unexpected exception.\n" + serviceRequest + "\n" + serviceResponse, e);
            }
        };
    }

    <Entity extends IXingYiClientEntity, View extends IXingYiView<Entity>> Function<ServiceResponse, IdAndValue<View>> makeIdAndValue(IXingYiRemoteAccessDetails<Entity, View> clientMaker, ServiceRequest serviceRequest) {
        return serviceResponse -> {
            try {

                DataAndJavaScript dataAndJavaScript = splitter.apply(serviceResponse);
                IXingYi<Entity, View> xingYi = factory.apply(dataAndJavaScript.javascript);
                Object mirror = xingYi.parse(dataAndJavaScript.data);
                IdAndValue result = xingYi.getIdAndValue(mirror, clientMaker);
                return result;
            } catch (Exception e) {
                throw new RuntimeException("Have thrown unexpected exception.\n" + serviceRequest + "\n" + serviceResponse, e);
            }
        };
    }
    Function<ServiceResponse, Boolean> makeBoolean(ServiceRequest serviceRequest) {
        return serviceResponse -> {
            try {
                return Boolean.parseBoolean(serviceResponse.body);
            } catch (Exception e) {
                throw new RuntimeException("Have thrown unexpected exception.\n" + serviceRequest + "\n" + serviceResponse, e);
            }
        };
    }


    @Override public <Entity extends IXingYiClientEntity, View extends IXingYiView<Entity>, Result> CompletableFuture<Result> primitive(
            IXingYiRemoteAccessDetails<Entity, View> clientMaker, String method, String url, Function<View, Result> fn) {
        ServiceRequest serviceRequest = new ServiceRequest(method, url.startsWith("/") ? protocolAndHost + url : url, List.of(), "");
        return service.apply(serviceRequest).thenApply(makeEntity(clientMaker, serviceRequest).andThen(fn));
    }
    @Override public <Entity extends IXingYiClientEntity, View extends IXingYiView<Entity>, Result> CompletableFuture<Optional<Result>> primitiveForOptional(
            IXingYiRemoteAccessDetails<Entity, View> clientMaker, String method, String url, Function<View, Result> fn) {
        ServiceRequest serviceRequest = new ServiceRequest(method, url.startsWith("/") ? protocolAndHost + url : url, List.of(), "");
        return service.apply(serviceRequest).thenApply(sr -> makeOptionalEntity(clientMaker, serviceRequest).apply(sr).map(fn));
    }
    @Override public <Entity extends IXingYiClientEntity, View extends IXingYiView<Entity>, Result> CompletableFuture<Boolean> primitiveForBoolean(String method, String url) {
        ServiceRequest serviceRequest = new ServiceRequest(method, url.startsWith("/") ? protocolAndHost + url : url, List.of(), "");
        return service.apply(serviceRequest).thenApply(makeBoolean(serviceRequest));
    }
    @Override public <Entity extends IXingYiClientEntity, View extends IXingYiView<Entity>, Result> CompletableFuture<IdAndValue<View>> primitiveForIdAndValue(IXingYiRemoteAccessDetails<Entity, View> clientMaker, String method, String url) {
        ServiceRequest serviceRequest = new ServiceRequest(method, url.startsWith("/") ? protocolAndHost + url : url, List.of(), "");
        return service.apply(serviceRequest).thenApply(makeIdAndValue(clientMaker, serviceRequest));
    }
    @Override public <Entity extends IXingYiClientEntity, View extends IXingYiView<Entity>, Result> CompletableFuture<Result>
    get(IXingYiRemoteAccessDetails<Entity, View> clientMaker, String id, Function<View, Result> fn) {
        return getUrlPattern(clientMaker.bookmark()).thenCompose(urlPattern -> primitive(clientMaker, "get", urlPattern.replace("{id}", id), fn));//TODO UrlEncoding
    }
    @Override public <Entity extends IXingYiClientEntity, View extends IXingYiView<Entity>, Result> CompletableFuture<Optional<Result>> getOptional(IXingYiRemoteAccessDetails<Entity, View> clientMaker, String id, Function<View, Result> fn) {
        return getUrlPattern(clientMaker.bookmark()).thenCompose(urlPattern -> primitiveForOptional(clientMaker, "get", urlPattern.replace("{id}", id), fn));
    }
    @Override public <Entity extends IXingYiClientEntity, View extends IXingYiView<Entity>> CompletableFuture<View>
    create(IXingYiRemoteAccessDetails<Entity, View> clientMaker, String id) {
        return getUrlPattern(clientMaker.bookmark()).thenCompose(urlPattern -> primitive(clientMaker, "post", urlPattern.replace("{id}", id), v -> v));
    }
    @Override public <Entity extends IXingYiClientEntity, View extends IXingYiView<Entity>> CompletableFuture<IdAndValue<View>> createWithoutId(IXingYiRemoteAccessDetails<Entity, View> clientMaker) {
        return getUrlPattern(clientMaker.bookmark()).thenCompose(urlPattern -> primitiveForIdAndValue(clientMaker, "post", urlPattern.replace("/{id}", "")));
    }
    @Override public <Entity extends IXingYiClientEntity, View extends IXingYiView<Entity>> CompletableFuture<Boolean> delete(IXingYiRemoteAccessDetails<Entity, View> clientMaker, String id) {
        return getUrlPattern(clientMaker.bookmark()).thenCompose(urlPattern -> primitiveForBoolean("delete", urlPattern.replace("{id}", id)));
    }
    @Override public <Entity extends IXingYiClientEntity, View extends IXingYiView<Entity>> CompletableFuture<View> edit(IXingYiRemoteAccessDetails<Entity, View> clientMaker, String id, Function<View, View> fn) {
        throw new RuntimeException("not implemented yet. Should had íd and value lens to root javascript and should then consider how to refactor''primitive'");
    }
}


