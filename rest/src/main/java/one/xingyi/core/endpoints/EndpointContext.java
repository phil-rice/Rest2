package one.xingyi.core.endpoints;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.val;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.javascript.JavascriptDetailsToString;
import one.xingyi.core.javascript.JavascriptStore;
import one.xingyi.core.marshelling.*;
import one.xingyi.core.mediatype.ServerMediaTypeContext;
import one.xingyi.core.utils.IdAndValue;

import java.util.List;
import java.util.function.Function;
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class EndpointContext<J> implements ServerMediaTypeContext<J> {
    public final JavascriptStore javascriptStore;
    public final JavascriptDetailsToString javascriptDetailsToString;
    public final IMergeJavascriptAndJson mergeJavascriptAndJson;
    public final JsonParserAndWriter<J> parserAndWriter;
    public final String protocol;


    public <Entity extends HasJson<ContextForJson>> String resultBody(ServiceRequest serviceRequest, String codeUrl, Entity entity) {
        ContextForJson context = ContextForJson.forServiceRequest(protocol, serviceRequest);
        val json = parserAndWriter.fromJ(entity.toJson(parserAndWriter, context));
        return resultBodyForJson(context.template(codeUrl), json);
    }
    public <Entity extends HasJsonWithLinks<ContextForJson, Entity>> String resultBodyWithLinks(ServiceRequest serviceRequest, String codeUrl, Entity entity, Function<Entity, String> stateFn) {
        ContextForJson context = ContextForJson.forServiceRequest(protocol, serviceRequest);
        String json = parserAndWriter.fromJ(entity.toJsonWithLinks(parserAndWriter, context, stateFn));
        return resultBodyForJson(context.template(codeUrl), json);
    }

    public <Entity extends HasJsonWithLinks<ContextForJson, Entity>> String resultBodyForIdAndValue(ServiceRequest serviceRequest, String codeUrl, IdAndValue<Entity> entity, Function<Entity, String> stateFn) {
        ContextForJson contextForJson = ContextForJson.forServiceRequest(protocol, serviceRequest);
        J j = IdAndValue.toJson(entity, parserAndWriter, contextForJson, stateFn);
        return resultBodyForJson(contextForJson.template(codeUrl), parserAndWriter.fromJ(j));
    }
    String resultBodyForJson(String codeUrl, String json) {
        String javascript = javascriptDetailsToString.apply(javascriptStore.find(List.of()));
        return mergeJavascriptAndJson.merge(codeUrl, javascript, json);
    }
    @Override public JsonParserAndWriter<J> parserAndWriter() { return parserAndWriter; }
    @Override public JavascriptStore javascriptStore() { return javascriptStore; }
    @Override public JavascriptDetailsToString javascriptDetailsToString() { return javascriptDetailsToString; }
    @Override public String protocol() { return protocol; }
}
