package one.xingyi.core.mediatype;
import one.xingyi.core.client.IXingYi;
import one.xingyi.core.client.IXingYiFactory;
import one.xingyi.core.client.LensLinesXingYi;
import one.xingyi.core.http.ServiceResponse;
import one.xingyi.core.marshelling.DataToBeSentToClient;
import one.xingyi.core.marshelling.IXingYiResponseSplitter;
import one.xingyi.core.marshelling.JsonParserAndWriter;
import one.xingyi.core.optics.lensLanguage.LensDefnStore;
import one.xingyi.core.sdk.IXingYiClientFactory;
import one.xingyi.core.sdk.IXingYiClientResource;
import one.xingyi.core.sdk.IXingYiView;
import one.xingyi.core.utils.Lists;
import one.xingyi.core.utils.Sets;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
abstract class SimpleClientMediaTypeDefn<ClientEntity extends IXingYiClientResource, ClientView extends IXingYiView<ClientEntity>> implements IMediaTypeClientDefn<ClientEntity, ClientView> {
    final String prefix;
    public SimpleClientMediaTypeDefn(String prefix, String entityName) {
        this.prefix = prefix + "." + entityName + ".";
    }
    @Override public String acceptHeader(Set<String> capabilities) {
        return prefix + Lists.join(Sets.sortedList(capabilities, String::compareTo), ".");
    }

}

class JsonAndJavascriptClientMediaTypeDefn<ClientEntity extends IXingYiClientResource, ClientView extends IXingYiView<ClientEntity>> extends SimpleClientMediaTypeDefn<ClientEntity, ClientView> {
    final Function<String, CompletableFuture<String>> getJavascript;
    final IXingYiClientFactory<ClientEntity, ClientView> makeEntity;
    final IXingYiFactory factory;

    public JsonAndJavascriptClientMediaTypeDefn(String entityName, Function<String, CompletableFuture<String>> getJavascript, IXingYiFactory factory, IXingYiClientFactory<ClientEntity, ClientView> makeEntity) {
        super(IMediaTypeConstants.jsonJavascriptPrefix, entityName);
        this.getJavascript = getJavascript;
        this.factory = factory;
        this.makeEntity = makeEntity;
    }

    @Override public CompletableFuture<ClientView> makeFrom(ServiceResponse serviceResponse) {
        DataToBeSentToClient dataToBeSentToClient = IXingYiResponseSplitter.rawSplit(serviceResponse);
        return getJavascript.apply(dataToBeSentToClient.defn).thenApply(javascript -> {
            IXingYi<IXingYiClientResource, IXingYiView<IXingYiClientResource>> xingYi = factory.apply(javascript);
            return makeEntity.make(xingYi, xingYi.parse(dataToBeSentToClient.data));
        });
    }
}
class JsonAndLensDefnClientMediaTypeDefn<ClientEntity extends IXingYiClientResource, ClientView extends IXingYiView<ClientEntity>> extends SimpleClientMediaTypeDefn<ClientEntity, ClientView> {
    private JsonParserAndWriter<Object> json;
    private final Function<String, CompletableFuture<String>> getDefn;
    private final Function<String, LensDefnStore> makeLensStore;
    final IXingYiClientFactory<ClientEntity, ClientView> makeEntity;

    public JsonAndLensDefnClientMediaTypeDefn(String entityName,
                                              JsonParserAndWriter<Object> json,
                                              Function<String, CompletableFuture<String>> getDefn,
                                              Function<String, LensDefnStore> makeLensStore,
                                              IXingYiClientFactory<ClientEntity, ClientView> makeEntity) {
        super(IMediaTypeConstants.jsonDefnPrefix, entityName);
        this.json = json;
        this.getDefn = getDefn;
        this.makeLensStore = makeLensStore;
        this.makeEntity = makeEntity;
    }
    @Override public CompletableFuture<ClientView> makeFrom(ServiceResponse serviceResponse) {
        DataToBeSentToClient dataToBeSentToClient = IXingYiResponseSplitter.rawSplit(serviceResponse);
        return getDefn.apply(dataToBeSentToClient.defn).thenApply(
                defnString -> {
                    LensDefnStore lensDefnStore = makeLensStore.apply(defnString);
                    return new LensLinesXingYi<ClientEntity, ClientView>(json, lensDefnStore);
                }).thenApply(x -> makeEntity.make(x, json.parse(dataToBeSentToClient.data)));
    }
}
