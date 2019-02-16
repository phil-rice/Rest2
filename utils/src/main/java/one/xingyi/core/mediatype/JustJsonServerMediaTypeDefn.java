package one.xingyi.core.mediatype;
import lombok.RequiredArgsConstructor;
import one.xingyi.core.endpoints.EndpointContext;
import one.xingyi.core.marshelling.ContextForJson;
import one.xingyi.core.marshelling.DataToBeSentToClient;
import one.xingyi.core.marshelling.JsonParserAndWriter;
import one.xingyi.core.marshelling.MakesFromJson;
import one.xingyi.core.sdk.IXingYiResource;
import one.xingyi.core.utils.IdAndValue;

import java.util.function.Function;
@RequiredArgsConstructor
public class JustJsonServerMediaTypeDefn<J, Entity extends IXingYiResource> implements IMediaTypeServerDefn<Entity> {
    final MakesFromJson<Entity> makesFromJson;
    final JsonParserAndWriter<J> parserAndWriter;

    @Override public Entity makeEntityFrom(String acceptHeader, String string) { return makesFromJson.fromJson(parserAndWriter, parserAndWriter.parse(string)); }
    @Override public DataToBeSentToClient makeDataAndDefn(ContextForJson context, Function<Entity, String> stateFn, Entity entity) {
        return new DataToBeSentToClient(entity.toJsonString(parserAndWriter, context), "");
    }
    @Override public DataToBeSentToClient makeDataAndDefn(ContextForJson context, Function<Entity, String> stateFn, IdAndValue<Entity> entity) {
        return new DataToBeSentToClient(parserAndWriter.fromJ(IdAndValue.toJson(entity, parserAndWriter, context)), "");
    }
}
