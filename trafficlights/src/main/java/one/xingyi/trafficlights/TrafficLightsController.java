package one.xingyi.trafficlights;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.marshelling.JsonParser;
import one.xingyi.core.store.ControllerUsingMap;
import one.xingyi.core.utils.IdAndValue;
import one.xingyi.trafficlights.server.companion.TrafficLightsCompanion;
import one.xingyi.trafficlights.server.controller.ITrafficLightsController;
import one.xingyi.trafficlights.server.domain.TrafficLights;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;


public class TrafficLightsController<J> extends ControllerUsingMap<TrafficLights> implements ITrafficLightsController {
    final JsonParser<J> parser;
    public TrafficLightsController(JsonParser<J> parser) {
        super("TrafficLights");
        this.parser = parser;
        store.put("1", new TrafficLights("1", "red", "someLocation"));
    }
    CompletableFuture<TrafficLights> map(String id, Function<TrafficLights, TrafficLights> fn) {
        TrafficLights t = store.get(id);
        if (t == null) throw new RuntimeException("Tried to access id: " + id + " which doesn't exist. Legal values are: " + store);
        TrafficLights newLight = fn.apply(t);
        store.put(id, newLight);
        System.out.println("created new traffic light " + id + "->" + newLight + "    "+ store);
        return CompletableFuture.completedFuture(newLight);
    }
    @Override public TrafficLights createWithoutIdRequestFrom(ServiceRequest serviceRequest) {

        return TrafficLightsCompanion.companion.fromJson(parser, parser.parse(serviceRequest.body));
    }
    @Override public CompletableFuture<IdAndValue<TrafficLights>> createWithoutId(TrafficLights trafficLights) {
        store.put(trafficLights.id(), trafficLights);
        return CompletableFuture.completedFuture(new IdAndValue<TrafficLights>(trafficLights.id(), trafficLights));
    }
    @Override public String stateFn(TrafficLights trafficLights) { return trafficLights.color(); }

    @Override protected TrafficLights prototype(String id) { return new TrafficLights(id, "red", ""); }
    @Override public CompletableFuture<TrafficLights> orange(String id) { return map(id, l -> l.withcolor("orange")); }
    @Override public CompletableFuture<TrafficLights> red(String id) { return map(id, l -> l.withcolor("red")); }
    @Override public CompletableFuture<TrafficLights> green(String id) { return map(id, l -> l.withcolor("green")); }
    @Override public CompletableFuture<TrafficLights> flashing(String id) { return map(id, l -> l.withcolor("flashing")); }
}
