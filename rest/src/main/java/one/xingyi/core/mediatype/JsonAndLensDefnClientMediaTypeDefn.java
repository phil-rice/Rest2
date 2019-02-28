package one.xingyi.core.mediatype;
import one.xingyi.core.client.LensLinesXingYi;
import one.xingyi.core.http.ServiceResponse;
import one.xingyi.core.marshelling.DataToBeSentToClient;
import one.xingyi.core.marshelling.IXingYiResponseSplitter;
import one.xingyi.core.marshelling.JsonParserAndWriter;
import one.xingyi.core.optics.lensLanguage.LensDefnStore;
import one.xingyi.core.sdk.IXingYiClientFactory;
import one.xingyi.core.sdk.IXingYiClientResource;
import one.xingyi.core.sdk.IXingYiView;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
public class JsonAndLensDefnClientMediaTypeDefn<J, ClientEntity extends IXingYiClientResource, ClientView extends IXingYiView<ClientEntity>> extends SimpleClientMediaTypeDefn<ClientEntity, ClientView> {
    private JsonParserAndWriter<J> json;
    private final Function<String, CompletableFuture<String>> getDefn;
    private final Function<String, LensDefnStore> makeLensStore;
    final IXingYiClientFactory<ClientEntity, ClientView> makeEntity;

    public JsonAndLensDefnClientMediaTypeDefn(String entityName,
                                              JsonParserAndWriter<J> json,
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
                    return new LensLinesXingYi<J, ClientEntity, ClientView>(json, lensDefnStore);
                }).thenApply(x -> makeEntity.make(x, json.parse(dataToBeSentToClient.data)));
    }
}
