package one.xingyi.core.utils;
import one.xingyi.core.names.ViewNames;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Supplier;
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

    public static <K,V> void addNotAllowingDuplicates(HashMap<K,V> map, K k, V v, BiFunction<K,V,String> error) {
     if (map.containsKey(k))
         throw new IllegalArgumentException(error.apply(k,map.get(k)));
     map.put(k, v);
    }
}
