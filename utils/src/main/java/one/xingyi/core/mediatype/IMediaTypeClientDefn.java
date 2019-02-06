package one.xingyi.core.mediatype;
import one.xingyi.core.client.IXingYiFactory;
import one.xingyi.core.http.ServiceResponse;
import one.xingyi.core.marshelling.JsonParserAndWriter;
import one.xingyi.core.optics.lensLanguage.LensLineParser;
import one.xingyi.core.optics.lensLanguage.LensStoreParser;
import one.xingyi.core.sdk.IXingYiClientFactory;
import one.xingyi.core.sdk.IXingYiClientResource;
import one.xingyi.core.sdk.IXingYiView;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public interface IMediaTypeClientDefn<ClientEntity extends IXingYiClientResource, ClientView extends IXingYiView<ClientEntity>> {
    /** Calculate an accept header for these capabilities (which are at the moment just a list of lensNames)
     * @param capabilities*/
    String acceptHeader(Set<String> capabilities);

    /** This is the body of the service response
     * @param serviceResponse*/
    CompletableFuture<ClientView> makeFrom(ServiceResponse serviceResponse);

    static <ClientEntity extends IXingYiClientResource, ClientView extends IXingYiView<ClientEntity>> IMediaTypeClientDefn<ClientEntity, ClientView>
    jsonAndJavascriptClient(String entityName, Function<String, CompletableFuture<String>> getJavascript, IXingYiFactory xingYiFactory, IXingYiClientFactory<ClientEntity, ClientView> makeEntity) {
        return new JsonAndJavascriptClientMediaTypeDefn<>(entityName, getJavascript, xingYiFactory, makeEntity);
    }
    static <ClientEntity extends IXingYiClientResource, ClientView extends IXingYiView<ClientEntity>> IMediaTypeClientDefn<ClientEntity, ClientView>
    jsonAndLensDefnClient(String entityName, JsonParserAndWriter<Object> json, Function<String, CompletableFuture<String>> getDefn,   IXingYiClientFactory<ClientEntity, ClientView> makeEntity) {
        return new JsonAndLensDefnClientMediaTypeDefn<ClientEntity, ClientView>(entityName, json, getDefn, LensStoreParser.simple(), makeEntity);
    }


}