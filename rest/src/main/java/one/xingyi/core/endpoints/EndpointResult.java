package one.xingyi.core.endpoints;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.http.ServiceResponse;
import one.xingyi.core.marshelling.ContextForJson;
import one.xingyi.core.marshelling.HasJson;
import one.xingyi.core.marshelling.HasJsonWithLinks;
import one.xingyi.core.utils.IdAndValue;
import one.xingyi.core.utils.Optionals;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
public interface EndpointResult<Result> extends BiFunction<ServiceRequest, Result, ServiceResponse> {
    ServiceResponse apply(ServiceRequest serviceRequest, Result result);
    static <J, Result extends HasJson<ContextForJson>> EndpointResult<Result> create(EndpointContext<J> context, String rootUrl, int statusCode) {
        return (sr, r) -> ServiceResponse.<J>jsonString(statusCode, context.resultBody(sr, rootUrl, r));
    }

    static <J, Entity extends HasJsonWithLinks<ContextForJson, Entity>> EndpointResult<IdAndValue<Entity>> createForIdAndvalue(EndpointContext<J> context, String rootUrl, int statusCode, Function<Entity, String> stateFn) {
        return (sr, r) -> ServiceResponse.<J>jsonString(statusCode, context.resultBodyForIdAndValue(sr, rootUrl, r, stateFn));
    }
    static <Result> EndpointResult<Result> createForNonEntity(int statusCode, Function<Result, String> jsonFn) {
        return (sr, r) -> ServiceResponse.jsonString(statusCode, jsonFn.apply(r));
    }

    static <J, Entity extends HasJson<ContextForJson>> EndpointResult<Optional<Entity>> createForOptional(EndpointContext<J> context, int statusCodeIfPresent, String rootUrl) {
        return (sr, r) -> Optionals.fold(r, () -> ServiceResponse.notFound(""), result -> ServiceResponse.jsonString(statusCodeIfPresent, context.resultBody(sr, rootUrl, result)));
    }
    static <J, Result extends HasJsonWithLinks<ContextForJson, Result>> EndpointResult<Result> createWithLinks(
            EndpointContext<J> context,
            int statusCode, String codeUrl,
            Function<Result, String> stateFn) {
        return (sr, r) -> ServiceResponse.<J>jsonString(statusCode, context.resultBodyWithLinks(sr, codeUrl, r, stateFn));
    }
    static <J, Result extends HasJsonWithLinks<ContextForJson, Result>> EndpointResult<Optional<Result>> createForOptionalWithLinks(EndpointContext<J> context, int statusCode, String rootUrl, Function<Result, String> stateFn) {
        return (sr, r) -> Optionals.fold(r,
                () -> ServiceResponse.notFound(""),
                result -> ServiceResponse.<J>jsonString(statusCode, context.resultBodyWithLinks(sr, rootUrl, result, stateFn)));
    }
}
