package one.xingyi.core.endpointDefn;
import lombok.RequiredArgsConstructor;
import one.xingyi.core.functions.Functions;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.sdk.IXingYiResource;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
@RequiredArgsConstructor
/** This is just one way of making the EndPointdefnData. There's nothing wrong with making the manually, and that might be the norm in your project
 * The assumptions here are that there is a backing store and that the end point gets the data out of the backing store using Req
 * Even more the sideeffects are simple functions and not kleislis.
 */
public class EndPointDefnFactory<State, Req, Entity extends IXingYiResource> {

    final String initialPath;
    final Function<ServiceRequest, Req> fromFn;
    final Function<Req, CompletableFuture<Entity>> getDataFn;
    final List<State> empty = List.of();
    Function<Entity, Integer> status200 = Functions.constant(200);

    public EndPointDefnData<State, Req, Entity> get(String name, String path) { return new EndPointDefnData<>(name, "get", initialPath + path, empty, fromFn, getDataFn, status200); }
    public EndPointDefnData<State, Req, Entity> post(String name, String path, Consumer<Req> sideeffect) {return new EndPointDefnData<>(name, "post", initialPath + path, empty, fromFn, Functions.sideeffect(sideeffect, getDataFn), status200); }
    public EndPointDefnData<State, Req, Entity> postState(String name, String path, List<State> legalStates, Consumer<Req> sideeffect) {
        return new EndPointDefnData<>(name, "post", initialPath + path, legalStates, fromFn, Functions.sideeffect(sideeffect, getDataFn), status200);
    }
    public EndPointDefnData<State, Req, Boolean> delete(String name, String path, List<State> legalStates, Function<Req, Boolean> sideeffect) {
        return new EndPointDefnData<State, Req, Boolean>(name, "delete", initialPath + path, empty, fromFn, sideeffect.<CompletableFuture<Boolean>>andThen(CompletableFuture::completedFuture), Functions.constant(200));
    }
}
