package one.xingyi.core.endpoints;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.val;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.javascript.JavascriptDetailsToString;
import one.xingyi.core.javascript.JavascriptStore;
import one.xingyi.core.marshelling.*;
import one.xingyi.core.utils.IdAndValue;

import java.util.List;
import java.util.function.Function;
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class EndpointContext<J> {
    public final JavascriptStore javascriptStore;
    public final JavascriptDetailsToString javascriptDetailsToString;
    public final IMergeJavascriptAndJson mergeJavascriptAndJson;
    public final JsonWriter<J> jsonWriter;
    public final JsonParser<J> jsonParser;
    public final String protocol;


    public <Entity extends HasJson<ContextForJson>> String resultBody(ServiceRequest serviceRequest, String rootUrl, Entity entity) {
        val json = jsonWriter.fromJ(entity.toJson(jsonWriter, ContextForJson.forServiceRequest(protocol, serviceRequest)));
        return resultBodyForJson(rootUrl, json);
    }
    public <Entity extends HasJsonWithLinks<ContextForJson, Entity>> String resultBodyWithLinks(ServiceRequest serviceRequest, String rootUrl, Entity entity, Function<Entity, String> stateFn) {
        ContextForJson context = ContextForJson.forServiceRequest(protocol, serviceRequest);
        String json = jsonWriter.fromJ(entity.toJsonWithLinks(jsonWriter, context, stateFn));
        val javascript = javascriptDetailsToString.apply(javascriptStore.find(List.of()));
        return mergeJavascriptAndJson.merge(rootUrl, javascript, json);
    }

    public <Entity extends HasJson<ContextForJson>> String resultBodyForIdAndValue(ServiceRequest serviceRequest, String rootUrl, IdAndValue<Entity> entity) {
        J j = IdAndValue.toJson(entity, jsonWriter, ContextForJson.forServiceRequest(protocol, serviceRequest));
        return resultBodyForJson(rootUrl, jsonWriter.fromJ(j));
    }
    public String resultBodyForJson(String rootUrl, String json) {
        val javascript = javascriptDetailsToString.apply(javascriptStore.find(List.of()));
        return mergeJavascriptAndJson.merge(rootUrl, javascript, json);
    }
}
