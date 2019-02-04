package one.xingyi.core.mediatype;
import lombok.RequiredArgsConstructor;
import one.xingyi.core.marshelling.ContextForJson;
import one.xingyi.core.marshelling.JsonParserAndWriter;
import one.xingyi.core.marshelling.MakesFromJson;
import one.xingyi.core.sdk.IXingYiResource;
@RequiredArgsConstructor
public class JustJsonServerMediaTypeDefn<J, Entity extends IXingYiResource> implements IMediaTypeServerDefn<Entity> {
    final MakesFromJson<Entity> makesFromJson;
    final JsonParserAndWriter<J> parserAndWriter;

    @Override public Entity makeEntityFrom(String acceptHeader, String string) { return makesFromJson.fromJson(parserAndWriter, parserAndWriter.parse(string)); }
    @Override public String makeStringFrom(ContextForJson context, Entity entity) { return parserAndWriter.toJson(entity, context); }
}
