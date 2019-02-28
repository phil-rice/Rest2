package one.xingyi.core;
import java.util.List;
import java.util.Optional;
public interface ISimpleMap<K, V> {
    List<K> keys();
    Optional<V> get(K k);
}
