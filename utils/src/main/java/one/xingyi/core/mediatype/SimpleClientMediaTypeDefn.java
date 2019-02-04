package one.xingyi.core.mediatype;
import one.xingyi.core.client.IXingYi;
import one.xingyi.core.client.IXingYiFactory;
import one.xingyi.core.http.ServiceResponse;
import one.xingyi.core.marshelling.ContextForJson;
import one.xingyi.core.marshelling.DataAndDefn;
import one.xingyi.core.marshelling.IXingYiResponseSplitter;
import one.xingyi.core.marshelling.JsonParserAndWriter;
import one.xingyi.core.sdk.IXingYiClientFactory;
import one.xingyi.core.sdk.IXingYiClientResource;
import one.xingyi.core.sdk.IXingYiView;
import one.xingyi.core.utils.Lists;
import one.xingyi.core.utils.Sets;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
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
        DataAndDefn dataAndDefn = IXingYiResponseSplitter.rawSplit(serviceResponse);
        return getJavascript.apply(dataAndDefn.defn).thenApply(javascript -> {
            IXingYi<IXingYiClientResource, IXingYiView<IXingYiClientResource>> xingYi = factory.apply(javascript);
            return makeEntity.make(xingYi, xingYi.parse(dataAndDefn.data));
        });
    }
}
class JsonAndLensDefnClientMediaTypeDefn<Defn, ClientEntity extends IXingYiClientResource, ClientView extends IXingYiView<ClientEntity>> extends SimpleClientMediaTypeDefn<ClientEntity, ClientView> {
    public JsonAndLensDefnClientMediaTypeDefn(String entityName, Function<String, CompletableFuture<Defn>> getDefn, Function<Defn, ClientView> makeEntity) {
        super(IMediaTypeConstants.jsonDefnPrefix, entityName);
    }
    @Override public CompletableFuture<ClientView> makeFrom(ServiceResponse serviceResponse) {
        throw new RuntimeException("not implemented yet");
    }
}
