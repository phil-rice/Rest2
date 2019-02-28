package one.xingyi.core.mediatype;
import lombok.RequiredArgsConstructor;
import one.xingyi.core.endpoints.EndpointContext;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.marshelling.*;
import one.xingyi.core.sdk.IXingYiResource;
import one.xingyi.core.utils.IdAndValue;

import java.util.function.Function;
@RequiredArgsConstructor
public class JustJsonServerMediaTypeDefn<J, Entity extends IXingYiResource& HasJsonWithLinks<ContextForJson,Entity>> implements IMediaTypeServerDefn<Entity> {
    final String protocol;
    final MakesFromJson<Entity> makesFromJson;
    final JsonParserAndWriter<J> parserAndWriter;

    @Override public Entity makeEntityFrom(String acceptHeader, String string) { return makesFromJson.fromJson(parserAndWriter, parserAndWriter.parse(string)); }
    @Override public ContextForJson makeContextForJson(ServiceRequest serviceRequest) {
        return ContextForJson.forServiceRequest(protocol, serviceRequest);
    }
    @Override public DataToBeSentToClient makeDataAndDefn(ContextForJson context, Function<Entity, String> stateFn, Entity entity) {
        return new DataToBeSentToClient(entity.toJsonString(parserAndWriter, context), "");
    }
    @Override public DataToBeSentToClient makeDataAndDefn(ContextForJson context, Function<Entity, String> stateFn, IdAndValue<Entity> entity) {
        return new DataToBeSentToClient(parserAndWriter.fromJ(IdAndValue.toJson(entity, parserAndWriter, context,stateFn)), "");
    }
}
