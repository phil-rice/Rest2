package one.xingyi.core.http;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.marshelling.*;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class ServiceResponse {
    public final int statusCode;
    public final String body;
    public final List<Header> headers;

    public static <J> ServiceResponse jsonString(int status, String json) { return new ServiceResponse(status, json, Arrays.asList(new Header("Content-type", "application/json"))); }
    public static ServiceResponse html(int status, String body) { return new ServiceResponse(status, body, Arrays.asList(new Header("Content-type", "text/html"))); }
    public static ServiceResponse notFound(String msg) { return ServiceResponse.html(404, msg); }
}