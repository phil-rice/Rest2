package one.xingyi.trafficlights;
import one.xingyi.core.annotations.State;
import one.xingyi.trafficlights.domain.TrafficLights;
@State
enum TrafficLightState implements XingYiState<TrafficLights> {
    red, orange, green, flashing
}
