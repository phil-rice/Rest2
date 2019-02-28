package one.xingyi.core.marshelling;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.state.StateData;
import one.xingyi.core.utils.Lists;
import one.xingyi.core.utils.Optionals;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public interface ContextForJson {
    String protocol();

    String template(String raw);
    String acceptHeader();
    static ContextForJson nullContext = new NullContext();
    static ContextForJson forServiceRequest(String protocol, ServiceRequest serviceRequest) { return new ServiceRequestContextForJson(protocol, serviceRequest);}
    <J, Entity> J links(JsonWriter<J> jsonWriter, Entity entity, Function<Entity, String> stateFn, Map<String, List<StateData>> stateMap);
}

class NullContext implements ContextForJson {

    @Override public String protocol() {
        return "";
    }
    @Override public String template(String raw) { return raw.replace("{host}", ""); }
    @Override public String acceptHeader() { return "";}
    @Override public <J, Entity> J links(JsonWriter<J> jsonWriter, Entity entity, Function<Entity, String> stateFn, Map<String, List<StateData>> stateMap) {
        return jsonWriter.makeObject();
    }
}

@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
class ServiceRequestContextForJson implements ContextForJson {
    final String protocol;
    final ServiceRequest serviceRequest;
    public String protocol() { return protocol;}
    @Override public String template(String raw) {
        return raw.replace("{host}", serviceRequest.header("host").map(s -> protocol + s).orElse(""));
    }
    @Override public String acceptHeader() {        return serviceRequest.header("accept").orElse("");}
    @Override public <J, Entity> J links(JsonWriter<J> jsonWriter, Entity entity, Function<Entity, String> stateFn, Map<String, List<StateData>> stateMap) {
        J selfLink = jsonWriter.makeObject("_self", serviceRequest.uri.toString());
        return Optionals.fold(Optional.ofNullable(stateMap.get(Optional.ofNullable(stateFn.apply(entity)).orElse(""))),
                () -> jsonWriter.makeList(List.of(selfLink)),
                list -> jsonWriter.makeList(Lists.insert(selfLink, Lists.map(list, sd -> jsonWriter.makeObject(sd.action, sd.link)))));

    }
}
