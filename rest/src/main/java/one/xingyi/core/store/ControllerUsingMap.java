package one.xingyi.core.store;

import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.utils.IdAndValue;
import one.xingyi.core.utils.RunnableWithException;
import one.xingyi.core.utils.WrappedException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;


public abstract class ControllerUsingMap<T> {
    public final Map<String, T> store;
    final String entityName;
    protected abstract T prototype(String id);

    public ControllerUsingMap(String entityName) {
        this.entityName = entityName;
        this.store = Collections.synchronizedMap(new HashMap<>());
    }

    public CompletableFuture<T> wrap(String id, RunnableWithException runnable) {
        WrappedException.wrap(runnable);
        return CompletableFuture.completedFuture(Optional.ofNullable(store.get(id)).orElseThrow(() -> new RuntimeException("Cannot find " + entityName + " with id: " + id)));
    }

    public CompletableFuture<T> put(IdAndValue<T> idAndEntity) {
        return wrap(idAndEntity.id, () -> store.put(idAndEntity.id, idAndEntity.t));
    }
    public CompletableFuture<T> get(String id) { return CompletableFuture.completedFuture(store.get(id)); }
    public CompletableFuture<Optional<T>> getOptional(String id) {
        return CompletableFuture.completedFuture(Optional.ofNullable(store.get(id))); }
    public CompletableFuture<Boolean> delete(String id) {
        store.remove(id);
        return CompletableFuture.completedFuture(true);
    }
    public CompletableFuture<T> createWithId(String id) {
        return wrap(id, () -> {
            T prototype = prototype(id);
            store.put(id, prototype);
        });
    }
    public CompletableFuture<IdAndValue<T>> createWithoutId(T t) {
        String id = store.size() + "";
        store.put(id, t);
        return CompletableFuture.completedFuture(new IdAndValue<>(id, t));
    }

    public T createWithoutIdRequestFrom(ServiceRequest serviceRequest) {
        return prototype(Integer.toString(store.size()));
    }


}