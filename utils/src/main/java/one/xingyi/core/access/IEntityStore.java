package one.xingyi.core.access;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface IEntityStore<T> extends IEntityRead<T> {

    static <T> IEntityStore<T> map(Map<String, T> map) { return new MapEntityStore<T>(map); }

}

@ToString
@EqualsAndHashCode
class MapEntityStore<T> implements IEntityStore<T> {
    final Object lock;
    final Map<String, T> map;
    final IEntityRead<T> read;

    public MapEntityStore(Map<String, T> map) {
        this.lock = new Object();
        this.map = map;
        this.read = IEntityRead.map(lock, map);
    }
    @Override public CompletableFuture<Optional<T>> read(String id) {
        return read.read(id);
    }
}
