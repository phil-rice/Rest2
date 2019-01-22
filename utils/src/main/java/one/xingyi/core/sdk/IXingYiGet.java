package one.xingyi.core.sdk;
import one.xingyi.core.http.ServiceRequest;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Function;
public interface IXingYiGet<From, Defn extends IXingYiEntityDefn, Entity extends IXingYiEntity> extends Function<From, CompletableFuture<Optional<Entity>>> {
    BiFunction<ServiceRequest, String, From> makeId();

    BiFunction<ServiceRequest, String, String> makeIdFromString = (sr, s) -> s;

}
