package one.xingyi.trafficlights;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.utils.IdAndValue;
import one.xingyi.trafficlights.server.domain.TrafficLights;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

//We have to create this manually because this IS the API
//All the wiring is done. The links are created for us, and the visitors are available in the generated interfaces so the client has access to us
//All the state methods can only be accessed after a get, because only then do we know what state we are in
//Which means we should ship the state with the data
//We need to have a function that defines the state
class TrafficLightStuff implements HowToDoTrafficLights {
    TrafficLightStore store = new TrafficLightStore();

    CompletableFuture<TrafficLights> sideeffect(String id, Runnable runnable) {
        runnable.run();
        return getFn(id);
    }
    private CompletableFuture<TrafficLights> getFn(String id) {
        return CompletableFuture.completedFuture(new TrafficLights(id, store.lights.get(id)));
    }

    @Override public CompletableFuture<TrafficLights> put(String id, TrafficLights entity) { return sideeffect(id, () -> store.lights.putIfAbsent(id, entity.color())); }
    @Override public CompletableFuture<TrafficLights> get(String id) { return getFn(id); }
    @Override public CompletableFuture<Boolean> delete(String id) { return CompletableFuture.completedFuture(store.lights.remove(id) != null); }
    @Override public CompletableFuture<TrafficLights> createWithid(String id, TrafficLights trafficLights) { return put(id, trafficLights); }
    @Override public CompletableFuture<IdAndValue<TrafficLights>> create(TrafficLights trafficLights) {
        String id = Integer.toString(store.lights.size());
        return createWithid(id, trafficLights).thenApply(tl -> new IdAndValue<>(id, tl));
    }
    @Override public CompletableFuture<TrafficLights> changeOrange(String id) { return sideeffect(id, () -> store.lights.put(id, "orange")); }
    @Override public CompletableFuture<TrafficLights> changeGreen(String id) { return sideeffect(id, () -> store.lights.put(id, "green")); }
    @Override public CompletableFuture<TrafficLights> changeRed(String id) { return sideeffect(id, () -> store.lights.put(id, "red")); }
    @Override public CompletableFuture<TrafficLights> changeFlashing(String id) { return sideeffect(id, () -> store.lights.put(id, "flashing")); }
}

public class App {

    static Function<ServiceRequest, String> extractId() {return sr -> "";}


    public static void main(String[] args) {
        TrafficLightStore store = new TrafficLightStore();

        TrafficLightStuff theActualApi = new TrafficLightStuff();

        //TrafficLightServer.use(someConfigAboutPortsAndStuff,theStateFn,theActualApi)

//        TrafficLightServer.simple().start();
        System.out.println("Started traffic lights");
    }
}
