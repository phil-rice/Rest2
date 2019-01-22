package one.xingyi.trafficlights;

import one.xingyi.core.annotations.State;
import one.xingyi.core.state.IState;
import one.xingyi.trafficlights.domain.TrafficLights;
@State
public interface TrafficLightState extends IState<ITrafficLightsDefn, TrafficLights> {
    String colour();
    @Override default String state(TrafficLights entity) {
        return entity.color();
    }
}
