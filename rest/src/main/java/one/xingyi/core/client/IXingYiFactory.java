package one.xingyi.core.client;
import lombok.RequiredArgsConstructor;
import one.xingyi.core.marshelling.JsonParser;
import one.xingyi.core.marshelling.JsonParserAndWriter;
import one.xingyi.core.optics.lensLanguage.LensStoreParser;
import one.xingyi.core.sdk.IXingYiClientResource;
import one.xingyi.core.sdk.IXingYiView;

import java.util.HashMap;
import java.util.Map;
public interface IXingYiFactory {
    <Entity extends IXingYiClientResource, View extends IXingYiView<Entity>> IXingYi<Entity, View> apply(String javascriptOrListOfLens);

    static IXingYiFactory simple = new IXingYiFactory() {
        @Override public <Entity extends IXingYiClientResource, View extends IXingYiView<Entity>> IXingYi<Entity, View> apply(String javascript) {
            return new DefaultXingYi<>(javascript);
        }
    };

    static IXingYiFactory xingYi = new XingYiCachedFactory();

    static <J> IXingYiFactory lensFactory(JsonParserAndWriter<J> parserAndWriter) {
        return new IXingYiFactory() {
            @Override public <Entity extends IXingYiClientResource, View extends IXingYiView<Entity>> IXingYi<Entity, View> apply(String javascriptOrListOfLens) {
                return new LensLinesXingYi<>(parserAndWriter, LensStoreParser.simple().apply(javascriptOrListOfLens));
            }
        };
    }

//    static IXingYiFactory fromJson(JsonParserAndWriter<Object> parser) { return new FromJsonFactory(parser);}
}

//@RequiredArgsConstructor
//class FromJsonFactory implements IXingYiFactory {
//    final JsonParserAndWriter<Object> parser;
//    @Override public <Entity extends IXingYiClientResource, View extends IXingYiView<Entity>> IXingYi<Entity, View> apply(String listOfLens) {
//        return new LensLinesXingYi<>(parser, listOfLens,);
//    }
//}
class XingYiCachedFactory implements IXingYiFactory {
    ThreadLocal<SingleThreadedXingYiCachedFactory> factory = new ThreadLocal<>() {//this might not work... Want to 'not share' these. Basically should get rid of defn soon!
        @Override protected SingleThreadedXingYiCachedFactory initialValue() {
            return new SingleThreadedXingYiCachedFactory();
        }
    };
    @Override public <Entity extends IXingYiClientResource, View extends IXingYiView<Entity>> IXingYi<Entity, View> apply(String javascript) {
        return factory.get().apply(javascript);
    }
}
class SingleThreadedXingYiCachedFactory implements IXingYiFactory {

    final Map<Integer, IXingYi> cache = new HashMap<>();
    @SuppressWarnings("unchecked")
    @Override public <Entity extends IXingYiClientResource, View extends IXingYiView<Entity>> IXingYi<Entity, View> apply(String javascript) {
        int hash = javascript.hashCode();
        IXingYi result = cache.get(hash);
        if (result != null) return result;
        result = new DefaultXingYi(javascript);
        cache.putIfAbsent(hash, result);
        return cache.get(hash);
    }
}
