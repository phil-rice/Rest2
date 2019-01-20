package one.xingyi.core.client;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
public interface IXingYiFactory extends Function<String, IXingYi> {
    IXingYi apply(String javascript);

    static IXingYiFactory simple = javascript -> new DefaultXingYi(javascript);

    static IXingYiFactory xingYi = new XingYiCachedFactory();
}
class XingYiCachedFactory implements IXingYiFactory {

    final Object lock = new Object();
    final Map<Integer, IXingYi> cache = new HashMap<>();
    @Override public IXingYi apply(String javascript) {
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
