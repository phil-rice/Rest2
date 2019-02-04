package one.xingyi.core.mediatype;
import one.xingyi.core.marshelling.ContextForJson;
import one.xingyi.core.marshelling.DataAndDefn;
import one.xingyi.core.marshelling.JsonParserAndWriter;
import one.xingyi.core.marshelling.MakesFromJson;
import one.xingyi.core.sdk.IXingYiResource;

import java.util.List;
public interface IMediaTypeServerDefn<Entity extends IXingYiResource> {

    Entity makeEntityFrom(String acceptHeader, String string);
    String makeStringFrom(ContextForJson context, Entity entity);


    static <J, Entity extends IXingYiResource> IXingYiServerMediaTypeDefn<Entity> jsonAndJavascriptServer(String entityName, MakesFromJson<Entity> makesFromJson, ServerMediaTypeContext<J> context) {
        return new JsonAndJavascriptServerMediaTypeDefn<>(entityName, makesFromJson, context);
    }
    static <J, Entity extends IXingYiResource> IMediaTypeServerDefn<Entity> justJson(String entityName, MakesFromJson<Entity> makesFromJson, JsonParserAndWriter<J> parserAndWriter) {
        return new JustJsonServerMediaTypeDefn<>(makesFromJson, parserAndWriter);
    }
    static <J, Entity extends IXingYiResource> IXingYiServerMediaTypeDefn<Entity> jsonAndLensDefnServer(String entityName, MakesFromJson<Entity> makesFromJson, ServerMediaTypeContext<J> context) {
        return new JsonAndLensDefnServerMediaTypeDefn<>(entityName, makesFromJson, context);
    }

}
