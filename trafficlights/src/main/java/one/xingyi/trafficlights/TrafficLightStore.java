package one.xingyi.trafficlights;
import one.xingyi.core.annotations.Get;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.sdk.IXingYiGet;
import one.xingyi.trafficlights.server.domain.TrafficLights;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
@Get
public class TrafficLightStore implements IXingYiGet<String, ITrafficLightsDefn, TrafficLights> {

    public final Map<String, String> lights = Collections.synchronizedMap(new HashMap<>());
    public TrafficLightStore() {

        lights.put("1", "red");
        lights.put("2", "orange");
        lights.put("3", "green");
        lights.put("4", "flashing");
    }

    @Override public BiFunction<ServiceRequest, String, String> makeId() { return IXingYiGet.makeIdFromString; }
    @Override public CompletableFuture<Optional<TrafficLights>> apply(String id) {
        return CompletableFuture.completedFuture(Optional.ofNullable(lights.get(id)).map(c -> new TrafficLights(id, c)));
    }
}
