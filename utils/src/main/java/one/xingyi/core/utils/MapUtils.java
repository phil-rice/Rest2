package one.xingyi.core.utils;
import java.util.*;
public class MapUtils {
    @SafeVarargs
    public static <K, V> Map<K, V> append(Map<K, V>... maps) {
        Map<K, V> result = new HashMap<>();
        for (Map<K, V> map : maps)
            result.putAll(map);
        return result;
    }
    public static <K, V> Map<K, V> appendKeepingOrder(List<Map<K, V>> maps) {
        Map<K, V> result = new LinkedHashMap<>();
        for (Map<K, V> map : maps)
            result.putAll(map);
        return result;
    }

    public static <K, V> void add(Map<K, List<V>> map, K k, V v) {
        List<V> list = map.get(k);
        if (list == null) {
            list = new ArrayList<>();
            map.put(k, list);
        }
        list.add(v);
    }

}
