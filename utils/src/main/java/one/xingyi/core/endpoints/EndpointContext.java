package one.xingyi.core.endpoints;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.val;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.javascript.JavascriptDetailsToString;
import one.xingyi.core.javascript.JavascriptStore;
import one.xingyi.core.marshelling.ContextForJson;
import one.xingyi.core.marshelling.HasJson;
import one.xingyi.core.marshelling.IXingYiResponseSplitter;
import one.xingyi.core.marshelling.JsonWriter;
import one.xingyi.core.utils.IdAndValue;

import java.util.List;
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class EndpointContext<J> {
    public final JavascriptStore javascriptStore;
    public final JavascriptDetailsToString javascriptDetailsToString;
    public final JsonWriter<J> jsonWriter;
    public final String protocol;


    public <Entity extends HasJson<ContextForJson>> String resultBody(ServiceRequest serviceRequest, Entity entity) {
        val json = jsonWriter.fromJ(entity.toJson(jsonWriter, ContextForJson.forServiceRequest(protocol, serviceRequest)));
        return resultBodyForJson(json);
    }
    public <Entity extends HasJson<ContextForJson>> String resultBodyForIdAndValue(ServiceRequest serviceRequest, IdAndValue<Entity> entity) {
        J j = IdAndValue.toJson(entity, jsonWriter, ContextForJson.forServiceRequest(protocol, serviceRequest));
        return resultBodyForJson(jsonWriter.fromJ(j));
    }
    public String resultBodyForJson(String json) {
        val javascript = javascriptDetailsToString.apply(javascriptStore.find(List.of()));
        return javascript + IXingYiResponseSplitter.marker + json;
    }
}
