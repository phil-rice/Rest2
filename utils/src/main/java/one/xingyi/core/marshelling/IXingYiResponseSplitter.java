package one.xingyi.core.marshelling;
import lombok.RequiredArgsConstructor;
import one.xingyi.core.Cache;
import one.xingyi.core.client.IXingYi;
import one.xingyi.core.client.IXingYiFactory;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.http.ServiceResponse;
import one.xingyi.core.sdk.IXingYiClientResource;
import one.xingyi.core.sdk.IXingYiRemoteAccessDetails;
import one.xingyi.core.sdk.IXingYiView;
import one.xingyi.core.utils.WrappedException;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
public interface IXingYiResponseSplitter extends Function<ServiceResponse, CompletableFuture<DataAndJavaScript>> {

    static String marker = "\n---------\n";
    static IXingYiResponseSplitter inLineOnlySplitter = new SimpleXingYiResponseSplitter();
    static IXingYiResponseSplitter splitter(Function<ServiceRequest, CompletableFuture<ServiceResponse>> service) { return new XingYiResponseSplitter(service);}
    static DataAndJavaScript rawSplit(ServiceResponse serviceResponse) {
        if (serviceResponse.statusCode >= 300)
            throw new UnexpectedResponse(serviceResponse);
        String body = serviceResponse.body;
        int index = body.indexOf(SimpleXingYiResponseSplitter.marker);
        if (index == -1)
            throw new UnexpectedResponse("no marker found", serviceResponse);
        String javascript = body.substring(0, index);
        String data = body.substring(index + marker.length());
        return new DataAndJavaScript(data, javascript);
    }

    //TODO Work out where to put this. Probably move it into httpService.template
    static <Entity extends IXingYiClientResource, View extends IXingYiView<Entity>, Result> CompletableFuture<Result> fromServiceResponse(IXingYiFactory factory, IXingYiResponseSplitter splitter, IXingYiRemoteAccessDetails<Entity, View> clientMaker, ServiceRequest serviceRequest, ServiceResponse serviceResponse, Function<View, Result> resultFn) {
        try {
            return splitter.apply(serviceResponse).thenApply(dataAndJavaScript -> {
                try {
                    IXingYi<Entity, View> xingYi = factory.<Entity, View>apply(dataAndJavaScript.javascript);
                    Object mirror = xingYi.parse(dataAndJavaScript.data);
                    return resultFn.apply(clientMaker.make(xingYi, mirror));
                } catch (Exception e) {
                    throw new RuntimeException("Have thrown unexpected exception.\n" + serviceRequest + "\n" + serviceResponse, e);
                }
            });
        } catch (Exception e) {
            throw new RuntimeException("Have thrown unexpected exception.\n" + serviceRequest + "\n" + serviceResponse, e);
        }
    }
}

class SimpleXingYiResponseSplitter implements IXingYiResponseSplitter {
    @Override public CompletableFuture<DataAndJavaScript> apply(ServiceResponse serviceResponse) {
        return CompletableFuture.completedFuture(IXingYiResponseSplitter.rawSplit(serviceResponse));
    }
}

//This latter will need to be generated like HttpService is, so as to decouple from the CompletableFuture
class XingYiResponseSplitter implements IXingYiResponseSplitter {
    final Function<ServiceRequest, CompletableFuture<ServiceResponse>> service;
    final Cache<String, CompletableFuture<ServiceResponse>> javascriptCache;
    public XingYiResponseSplitter(Function<ServiceRequest, CompletableFuture<ServiceResponse>> service) {
        this.service = service;
        this.javascriptCache = Cache.dumbCache(url -> service.apply(new ServiceRequest("get", url, List.of(), "")));
    }
    @Override public CompletableFuture<DataAndJavaScript> apply(ServiceResponse serviceResponse) {
        DataAndJavaScript dataAndJavaScriptLinks = IXingYiResponseSplitter.rawSplit(serviceResponse);
        String urlOrJavascript = dataAndJavaScriptLinks.javascript;
        if (urlOrJavascript.startsWith("http") || urlOrJavascript.startsWith("/")) {
            return javascriptCache.apply(urlOrJavascript).thenApply(sr -> {
                checkServiceResponse(serviceResponse, urlOrJavascript, sr);
                return sr.body;
            }).thenApply(javascript -> new DataAndJavaScript(dataAndJavaScriptLinks.data, javascript)).
                    exceptionally(WrappedException.wrapFnWithE(e -> {javascriptCache.invalidate(urlOrJavascript); throw e;}));
        } else return CompletableFuture.completedFuture(dataAndJavaScriptLinks);

    }
    private void checkServiceResponse(ServiceResponse serviceResponse, String url, ServiceResponse sr) {
        if (sr.statusCode >= 300) {
            javascriptCache.invalidate(url);
            throw new RuntimeException(
                    "Unexpected response getting javascript: " + serviceResponse +
                            "\nOriginal response was" + serviceResponse +
                            "\nJavascript url was " + url +
                            "\nJavascript response" + sr);
        }
    }
}