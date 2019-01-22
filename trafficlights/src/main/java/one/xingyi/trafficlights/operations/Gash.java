package one.xingyi.trafficlights.operations;
import lombok.RequiredArgsConstructor;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.utils.Lists;
import one.xingyi.core.utils.Sets;
import one.xingyi.trafficlights.TrafficLightStore;
import one.xingyi.trafficlights.domain.TrafficLights;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

@RequiredArgsConstructor
public class Gash {
    final TrafficLightStore store;
    Map<String, String> lights() {return store.lights;}

    final VerbFactory<String, String, TrafficLights> make = new VerbFactory<>("/lights", (sr, s) -> s, id -> CompletableFuture.completedFuture(new TrafficLights(id, lights().get(id))));

    final PostDataWithId<String, TrafficLights> create = make.postDataWithId("", id -> lights().put(id, "red"));
    final DeleteData<String> delete = make.deleteData("/list/<id>", id -> lights().remove(id));

    final StateAwarePostData<String, String, TrafficLights> changeRed = make.stateAwarePost("/light/<id>/red", List.of("flashing"), id -> lights().put(id, "red"));
    final StateAwarePostData<String, String, TrafficLights> changeOrange = make.stateAwarePost("/light/<id>/orange", List.of("red"), id -> lights().put(id, "orange"));
    final StateAwarePostData<String, String, TrafficLights> changeGreen = make.stateAwarePost("/light/<id>/green", List.of("orange"), id -> lights().put(id, "green"));
    final StateAwarePostData<String, String, TrafficLights> changeFlashing = make.stateAwarePost("/light/<id>/flashing", List.of("green"), id -> lights().put(id, "flashing"));
    final SearchData<String, TrafficLights> search = new SearchData<>("/list/search",
            Map.of("color", "returns all lights with this colour"),
            (sr, params) -> params.get("color"),
            color -> CompletableFuture.completedFuture(
                    Lists.collect(Sets.toList(lights().entrySet()), e -> e.getValue().equalsIgnoreCase(color), e -> new TrafficLights(e.getKey(), e.getValue()))));
}

@RequiredArgsConstructor
class VerbFactory<State, From, To> {
    final String rootPath;
    final BiFunction<ServiceRequest, String, From> fromFn;
    final Function<From, CompletableFuture<To>> fn;
    public GetData<From, To> getData(String path) {return new GetData<>(rootPath + path, fromFn, fn);}
    public DeleteData<From> deleteData(String path, Consumer<From> sideeffect) {return new DeleteData<>(rootPath + path, fromFn, sideeffect);}
    public PostData<From, To> postData(String path, Function<ServiceRequest, From> fromFn, Consumer<From> sideeffect) {return new PostData<>(rootPath + path, fromFn, sideeffect, fn);}
    public PostDataWithId<From, To> postDataWithId(String path, Consumer<From> sideeffect) {return new PostDataWithId<>(rootPath + path, fromFn, sideeffect, fn);}
    public StateAwarePostData<State, From, To> stateAwarePost(String path, List<State> legalState, Consumer<From> sideeffect) { return new StateAwarePostData<State, From, To>(rootPath + path, legalState, fromFn, sideeffect, fn); }
}
@RequiredArgsConstructor
class GetData<From, To> {
    final String path;
    final BiFunction<ServiceRequest, String, From> fromFn;
    final Function<From, CompletableFuture<To>> fn;
}
@RequiredArgsConstructor
class PostData<From, To> {
    final String path;
    final Function<ServiceRequest, From> fromFn;
    final Consumer<From> sideeffect;
    final Function<From, CompletableFuture<To>> fn;
}
@RequiredArgsConstructor
class DeleteData<From> {
    final String path;
    final BiFunction<ServiceRequest, String, From> fromFn;
    final Consumer<From> sideeffect;
}
@RequiredArgsConstructor
class PostDataWithId<From, To> {
    final String path;
    final BiFunction<ServiceRequest, String, From> fromFn;
    final Consumer<From> sideeffect;
    final Function<From, CompletableFuture<To>> fn;
}
@RequiredArgsConstructor
class StateAwarePostData<State, From, To> {
    final String path;
    final List<State> legalStates;
    final BiFunction<ServiceRequest, String, From> fromFn;
    final Consumer<From> sideeffect;
    final Function<From, CompletableFuture<To>> fn;
}
@RequiredArgsConstructor
class SearchData<Search, To> {
    final String path; //for example "/list"
    final Map<String, String> allowedSearchParamsAndDescription; //the description is just for humans
    final BiFunction<ServiceRequest, Map<String, String>, Search> searchReqFn; //this takes the params and presents them as a map
    final Function<Search, CompletableFuture<List<To>>> searchFn;
}

