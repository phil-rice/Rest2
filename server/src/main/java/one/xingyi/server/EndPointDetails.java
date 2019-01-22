package one.xingyi.server;
import lombok.RequiredArgsConstructor;
import one.xingyi.core.endpoints.EndPointFactory;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.sdk.IXingYiEntity;
import one.xingyi.core.sdk.IXingYiServesEntityCompanion;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Function;
@RequiredArgsConstructor
public class EndPointDetails<From, T extends IXingYiEntity> {
    public final IXingYiServesEntityCompanion<?, T> companion;
    public final BiFunction<ServiceRequest, String, From> regFn;
    public final Function<From, CompletableFuture<Optional<T>>> getFn;

    public EndPointFactory make(){
        return EndPointFactorys.optionalBookmarked(companion.bookmarkAndUrl().urlPattern, regFn, getFn);
    }
}
