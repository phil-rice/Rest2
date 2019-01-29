package one.xingyi.core.client;
import one.xingyi.core.sdk.IXingYiClientEntity;
import one.xingyi.core.sdk.IXingYiView;

import java.util.HashMap;
import java.util.Map;
public interface IXingYiFactory {
    <Entity extends IXingYiClientEntity, View extends IXingYiView<Entity>> IXingYi<Entity, View> apply(String javascript);

    static IXingYiFactory simple = new IXingYiFactory() {
        @Override public <Entity extends IXingYiClientEntity, View extends IXingYiView<Entity>> IXingYi<Entity, View> apply(String javascript) {
            return new DefaultXingYi<>(javascript);
        }
    };

    static IXingYiFactory xingYi = new XingYiCachedFactory();
}
class XingYiCachedFactory implements IXingYiFactory {

    final Object lock = new Object();
    final Map<Integer, IXingYi> cache = new HashMap<>();
    @SuppressWarnings("unchecked")
    @Override public <Entity extends IXingYiClientEntity, View extends IXingYiView<Entity>> IXingYi<Entity, View> apply(String javascript) {
        int hash = javascript.hashCode();
        synchronized (lock) {
            IXingYi result = cache.get(hash);
            if (result != null) return result;
        }
        DefaultXingYi result = new DefaultXingYi(javascript);
        synchronized (lock) {
            cache.putIfAbsent(hash, result);
            return cache.get(hash);
        }
    }
}
