package one.xingyi.core.mediatype;
import lombok.RequiredArgsConstructor;
import one.xingyi.core.marshelling.ContextForJson;
import one.xingyi.core.marshelling.JsonParserAndWriter;
import one.xingyi.core.marshelling.MakesFromJson;
import one.xingyi.core.sdk.IXingYiResource;

import java.util.function.Function;
@RequiredArgsConstructor
public class JustJsonServerMediaTypeDefn<J, Entity extends IXingYiResource> implements IMediaTypeServerDefn<Entity> {
    final MakesFromJson<Entity> makesFromJson;
    final JsonParserAndWriter<J> parserAndWriter;

    @Override public Entity makeEntityFrom(String acceptHeader, String string) { return makesFromJson.fromJson(parserAndWriter, parserAndWriter.parse(string)); }
    @Override public String makeStringFrom(ContextForJson context, Function<String, String> entityEnvelopeFn, Function<Entity, String> stateFn, Entity entity) {
        return entity.toJsonString(parserAndWriter, context);
    }
}
