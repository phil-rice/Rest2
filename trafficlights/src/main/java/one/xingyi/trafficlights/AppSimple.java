package one.xingyi.trafficlights;
import lombok.val;
import one.xingyi.core.endpointDefn.EndPointDefnFactory;
import one.xingyi.core.endpointDefn.LinkData;
import one.xingyi.core.endpointDefn.LinkDataToJson;
import one.xingyi.core.http.Header;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.marshelling.ContextForJson;
import one.xingyi.core.marshelling.JsonWriter;
import one.xingyi.core.utils.Lists;
import one.xingyi.json.Json;
import one.xingyi.trafficlights.domain.TrafficLights;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Function;
public class AppSimple {

    static Function<ServiceRequest, String> extractId() {return sr -> "";}

    public static void main(String[] args) {
        TrafficLightStore store = new TrafficLightStore();

        final val make = new EndPointDefnFactory<String, String, TrafficLights>("{host}/light", extractId(), id -> CompletableFuture.completedFuture(new TrafficLights(id, store.lights.get(id))));

        val get = make.get("_self", "/{id}");
        val create = make.post("create new, when know new id", "/{id}", id -> store.lights.put(id, "red"));
        val delete = make.delete("delete", "/{id}", List.of(), id -> {store.lights.remove(id); return true;});
        val changeRed = make.postState("change to be red", "/{id}/red", List.of("flashing"), id -> store.lights.put(id, "red"));
        val changeOng = make.postState("change to be red", "/{id}/orange", List.of("red"), id -> store.lights.put(id, "orange"));
        val changeGrn = make.postState("change to be red", "/{id}/green", List.of("orange"), id -> store.lights.put(id, "green"));
        val changeFls = make.postState("change to be red", "/{id}/flashing", List.of("green"), id -> store.lights.put(id, "flashing"));


        List<LinkData<String>> defns = List.of(get, create, delete, changeRed, changeOng, changeGrn, changeFls);
        JsonWriter<Object> writer = Json.simple;
        BiFunction<ContextForJson, LinkData<String>, Object> toJson = (contextm, ld) -> writer.makeObject(ld.name(), writer.makeObject("href", contextm.template(ld.templatedPath())), "method", ld.method(), "states", ld.legalStates()); ;
        LinkDataToJson<Object, TrafficLights, String> linkDataToJson = LinkDataToJson.<Object, TrafficLights, String>fromLinkdata(TrafficLights::color, defns, toJson);

        System.out.println(create);
        System.out.println(changeRed);
        System.out.println(changeOng);
        ContextForJson contextForJson = ContextForJson.forServiceRequest("http://", new ServiceRequest("get", "/someurl", List.of(new Header("host", "someHost")), ""));
        System.out.println(Lists.map(linkDataToJson.apply(contextForJson, new TrafficLights("1", store.lights.get("1"))), t -> writer.fromJ(t)));
        System.out.println(Lists.map(linkDataToJson.apply(contextForJson, new TrafficLights("2", store.lights.get("2"))), t -> writer.fromJ(t)));
        System.out.println(Lists.map(linkDataToJson.apply(contextForJson, new TrafficLights("3", store.lights.get("3"))), t -> writer.fromJ(t)));

//        TrafficLightServer.simple().start();
        System.out.println("Started traffic lights");
    }
}
