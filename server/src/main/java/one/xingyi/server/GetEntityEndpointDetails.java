package one.xingyi.server;
import lombok.RequiredArgsConstructor;
import one.xingyi.core.endpoints.EndPointFactory;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.sdk.IXingYiEntity;
import one.xingyi.core.sdk.IXingYiEntityDefn;
import one.xingyi.core.sdk.IXingYiGet;
import one.xingyi.core.sdk.IXingYiServesEntityCompanion;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Function;
@RequiredArgsConstructor
public class GetEntityEndpointDetails<From, Defn extends IXingYiEntityDefn, Entity extends IXingYiEntity> {
    public final IXingYiServesEntityCompanion<Defn, Entity> companion;
    public final IXingYiGet<From, Defn, Entity> get;

    public EndPointFactory make() {
        return EndPointFactorys.optionalBookmarked(companion.bookmarkAndUrl().urlPattern, get.makeId(), get);
    }
}
