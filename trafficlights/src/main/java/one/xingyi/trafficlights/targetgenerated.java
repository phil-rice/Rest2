package one.xingyi.trafficlights;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.http.ServiceResponse;
import one.xingyi.trafficlights.client.view.ColourView;
import one.xingyi.trafficlights.server.domain.TrafficLights;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Function;


//For server side
//This would be created, and then I need to implement it.
//Each item was created by an annotation
interface HowToDoTrafficLights {
    CompletableFuture<TrafficLights> put(String id, TrafficLights ColourView);
    CompletableFuture<TrafficLights> get(String id);
    CompletableFuture<Boolean> delete(String id);
    CompletableFuture<TrafficLights> createWithid(String id, TrafficLights ColourView);
    CompletableFuture<IdAndValue<TrafficLights>> create(TrafficLights trafficLights);
    CompletableFuture<TrafficLights> changeOrange(String id);
    CompletableFuture<TrafficLights> changeGreen(String id);
    CompletableFuture<TrafficLights> changeRed(String id);
    CompletableFuture<TrafficLights> changeFlashing(String id);
}
interface HowToDoColourViewFns {
    Function<IdAndValue<TrafficLights>, CompletableFuture<TrafficLights>> put();
    Function<String, CompletableFuture<TrafficLights>> get();
    Function<String, CompletableFuture<Boolean>> delete();
    Function<IdAndValue<TrafficLights>, CompletableFuture<TrafficLights>> createWithid();
    Function<TrafficLights, CompletableFuture<IdAndValue<TrafficLights>>> create();
    Function<String, CompletableFuture<TrafficLights>> changeOrange();
    Function<String, CompletableFuture<TrafficLights>> changeGreen();
    Function<String, CompletableFuture<TrafficLights>> changeRed();
    Function<String, CompletableFuture<TrafficLights>> changeFlashing();
}

//for client side

interface ModifiedClient {
    ColourViewDetails makeView(Function<ServiceRequest, CompletableFuture<ServiceResponse>> service);
}

interface ColourViewDetails {
    <T> CompletableFuture<T> get(String id, Function<ColourView, T> fn);
    <T> CompletableFuture<T> get(String id, BiFunction<ColourViewVisitor<T>, ColourView, T> fn);
    CompletableFuture<IdAndValue<TrafficLights>> post(one.xingyi.trafficlights.client.view.ColourView ColourView);
    <T> CompletableFuture<T> put(String id, one.xingyi.trafficlights.client.view.ColourView ColourView);
    <T> CompletableFuture<T> delete(String id, Function<Boolean, T> fn);
}

//Only available from visitor
interface RedColourView extends ColourViewDetails {
    CompletableFuture<one.xingyi.trafficlights.client.view.ColourView> changeOrange(String id);
    <T> CompletableFuture<T> changeOrange(String id, Function<one.xingyi.trafficlights.client.view.ColourView, T> fn);
    <T> CompletableFuture<T> changeOrange(String id, BiFunction<ColourViewVisitor<T>, one.xingyi.trafficlights.client.view.ColourView, T> fn);
}
interface OrangeColourView extends ColourViewDetails {
    CompletableFuture<one.xingyi.trafficlights.client.view.ColourView> changeGreen(String id);
    <T> CompletableFuture<T> changeGreen(String id, Function<one.xingyi.trafficlights.client.view.ColourView, T> fn);
    <T> CompletableFuture<T> changeGreen(String id, BiFunction<ColourViewVisitor<T>, one.xingyi.trafficlights.client.view.ColourView, T> fn);
}
interface GreenColourView extends ColourViewDetails {
    CompletableFuture<one.xingyi.trafficlights.client.view.ColourView> changeFlashing(String id);
    <T> CompletableFuture<T> changeFlashing(String id, Function<one.xingyi.trafficlights.client.view.ColourView, T> fn);
    <T> CompletableFuture<T> changeFlashing(String id, BiFunction<ColourViewVisitor<T>, one.xingyi.trafficlights.client.view.ColourView, T> fn);
}
interface FlashingColourView extends ColourViewDetails {
    CompletableFuture<one.xingyi.trafficlights.client.view.ColourView> changeRed(String id);
    <T> CompletableFuture<T> changeRed(String id, Function<one.xingyi.trafficlights.client.view.ColourView, T> fn);
    <T> CompletableFuture<T> changeRed(String id, BiFunction<ColourViewVisitor<T>, one.xingyi.trafficlights.client.view.ColourView, T> fn);
}
interface ColourViewVisitor<T> {
    CompletableFuture<T> redTrafficLight(BiFunction<RedColourView, one.xingyi.trafficlights.client.view.ColourView, T> fn);
    CompletableFuture<T> orangeTrafficLight(BiFunction<OrangeColourView, one.xingyi.trafficlights.client.view.ColourView, T> fn);
    CompletableFuture<T> greenTrafficLight(BiFunction<GreenColourView, one.xingyi.trafficlights.client.view.ColourView, T> fn);
    CompletableFuture<T> flashingTrafficLight(BiFunction<FlashingColourView, one.xingyi.trafficlights.client.view.ColourView, T> fn);
}

