package one.xingyi.trafficlights;
import one.xingyi.core.functions.Functions;
import one.xingyi.core.utils.IdAndValue;
import one.xingyi.trafficlights.server.controller.ITrafficLightsController;
import one.xingyi.trafficlights.server.domain.TrafficLights;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
public class TrafficLightsController implements ITrafficLightsController {
    public final Map<String, TrafficLights> lights = Collections.synchronizedMap(new HashMap<>());
    public TrafficLightsController() { lights.put("1", new TrafficLights("1", "red", "someLocation")); }
    CompletableFuture<TrafficLights> wrap(String id, Runnable sideeffect) {
        sideeffect.run();
        return CompletableFuture.completedFuture(lights.get(id));
    }
    CompletableFuture<TrafficLights> map(String id, Function<TrafficLights, TrafficLights> fn) {
        TrafficLights newLight = fn.apply(lights.get(id));
        lights.put(id, newLight);
        return CompletableFuture.completedFuture(newLight);
    }

    @Override public String stateFn(TrafficLights trafficLights) { return trafficLights.color(); }
    @Override public CompletableFuture<TrafficLights> put(IdAndValue<TrafficLights> idAndTrafficLights) {
        return wrap(idAndTrafficLights.id, () -> lights.put(idAndTrafficLights.id, idAndTrafficLights.t));
    }
    @Override public CompletableFuture<Optional<TrafficLights>> getOptional(String id) { return CompletableFuture.completedFuture(Optional.ofNullable(lights.get(id))); }
    @Override public CompletableFuture<Boolean> delete(String id) { lights.remove(id); return CompletableFuture.completedFuture(true); }
    @Override public CompletableFuture<TrafficLights> create(String id) {
        return wrap(id, () -> lights.put(id, new TrafficLights(id, "red", "")));
    }
    @Override public CompletableFuture<IdAndValue<TrafficLights>> create() {
        String id = lights.size() + "";
        TrafficLights light = new TrafficLights(id, "red", "");
        lights.put(id, light);
        return CompletableFuture.completedFuture(new IdAndValue<>(id, light));
    }
    @Override public CompletableFuture<TrafficLights> orange(String id) { return map(id, l -> l.withcolor("orange")); }
    @Override public CompletableFuture<TrafficLights> red(String id) {
        return map(id, l -> l.withcolor("red"));
    }
    @Override public CompletableFuture<TrafficLights> green(String id) {
        return map(id, l -> l.withcolor("green"));
    }
    @Override public CompletableFuture<TrafficLights> flashing(String id) {
        return map(id, l -> l.withcolor("flashing"));
    }
}
