package one.xingyi.core.http;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.marshelling.ContextForJson;
import one.xingyi.core.marshelling.HasJson;
import one.xingyi.core.marshelling.IXingYiResponseSplitter;
import one.xingyi.core.marshelling.JsonTC;

import java.util.Arrays;
import java.util.List;

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class ServiceResponse {
    public final int statusCode;
    public final String body;
    public final List<Header> headers;

    public static <J> ServiceResponse json(JsonTC<J> jsonTc, ContextForJson context, int status, HasJson<ContextForJson> entity) {
        return new ServiceResponse(status, jsonTc.toJson(entity, context), Arrays.asList(new Header("Content-type", "application/json")));
    }
    public static <J> ServiceResponse javascriptAndJson(JsonTC<J> jsonTc, ContextForJson context, int status, HasJson<ContextForJson> entity, String javascript) {
        return new ServiceResponse(status, javascript + IXingYiResponseSplitter.marker + jsonTc.toJson(entity, context), Arrays.asList(new Header("Content-type", "application/json")));
    }

    public static ServiceResponse html(int status, String body) {
        return new ServiceResponse(status, body, Arrays.asList(new Header("Content-type", "text/html")));

    }
    public static ServiceResponse notFound(String msg) { return ServiceResponse.html(404, msg); }
}