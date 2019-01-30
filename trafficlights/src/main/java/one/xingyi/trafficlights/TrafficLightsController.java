package one.xingyi.trafficlights;
import one.xingyi.core.store.ControllerUsingMap;
import one.xingyi.core.utils.IdAndValue;
import one.xingyi.trafficlights.server.controller.ITrafficLightsController;
import one.xingyi.trafficlights.server.domain.TrafficLights;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
public class TrafficLightsController extends ControllerUsingMap<TrafficLights> implements ITrafficLightsController {
    public TrafficLightsController() {
        super("TrafficLights");
        store.put("1", new TrafficLights("1", "red", "someLocation"));
    }
    CompletableFuture<TrafficLights> map(String id, Function<TrafficLights, TrafficLights> fn) {
        TrafficLights newLight = fn.apply(store.get(id));
        store.put(id, newLight);
        return CompletableFuture.completedFuture(newLight);
    }

    @Override public String stateFn(TrafficLights trafficLights) { return trafficLights.color(); }

    @Override protected TrafficLights prototype(String id) { return new TrafficLights(id, "red", ""); }
    @Override public CompletableFuture<TrafficLights> orange(String id) { return map(id, l -> l.withcolor("orange")); }
    @Override public CompletableFuture<TrafficLights> red(String id) { return map(id, l -> l.withcolor("red")); }
    @Override public CompletableFuture<TrafficLights> green(String id) { return map(id, l -> l.withcolor("green")); }
    @Override public CompletableFuture<TrafficLights> flashing(String id) { return map(id, l -> l.withcolor("flashing")); }
}
