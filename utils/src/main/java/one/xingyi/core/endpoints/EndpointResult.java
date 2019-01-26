package one.xingyi.core.endpoints;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.http.ServiceResponse;
import one.xingyi.core.marshelling.ContextForJson;
import one.xingyi.core.marshelling.HasJson;
import one.xingyi.core.utils.Function3;
import one.xingyi.core.utils.IdAndValue;
import one.xingyi.core.utils.Optionals;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
public interface EndpointResult<Result> extends BiFunction<ServiceRequest, Result, ServiceResponse> {
    ServiceResponse apply(ServiceRequest serviceRequest, Result result);
    static <J, Result extends HasJson<ContextForJson>> EndpointResult<Result> create(EndpointContext context, int statusCode) {
        return (sr, r) -> ServiceResponse.jsonString(statusCode, context.resultBody(sr, r));
    }
    static <J, Entity extends HasJson<ContextForJson>> EndpointResult<IdAndValue<Entity>> createForIdAndvalue(EndpointContext context, int statusCode) {
        return (sr, r) -> ServiceResponse.jsonString(statusCode, context.resultBodyForIdAndValue(sr, r));
    }
    static <Result> EndpointResult<Result> create(int statusCode, Function<Result, String> jsonFn) {
        return (sr, r) -> ServiceResponse.jsonString(statusCode, jsonFn.apply(r));
    }

    static <J, Entity extends HasJson<ContextForJson>> EndpointResult<Optional<Entity>> createForOptional(EndpointContext context, int statusCodeIfPresent) {
        return (sr, r) -> Optionals.fold(r, () -> ServiceResponse.notFound(""), result -> ServiceResponse.jsonString(statusCodeIfPresent, context.resultBody(sr, result)));
    }
}
