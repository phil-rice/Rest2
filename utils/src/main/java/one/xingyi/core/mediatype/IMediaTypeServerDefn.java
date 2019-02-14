package one.xingyi.core.mediatype;
import one.xingyi.core.marshelling.ContextForJson;
import one.xingyi.core.marshelling.JsonParserAndWriter;
import one.xingyi.core.marshelling.MakesFromJson;
import one.xingyi.core.optics.lensLanguage.LensLine;
import one.xingyi.core.sdk.IXingYiResource;

import java.util.List;
import java.util.function.Function;
public interface IMediaTypeServerDefn<Entity extends IXingYiResource> {

    /** This parses the string and returns an entity. It can throw exceptions if the string is malformed */
    Entity makeEntityFrom(String acceptHeader, String string);

    /** This turns the entity into a string suitable for a payload. The envelope function wraps the entity in other things if needed (normally just used with id/value, or Function.identity()  */
    String makeStringFrom(ContextForJson context, Function<String, String> entityEnvelopeFn, Function<Entity, String> stateFn, Entity entity);


    static <J, Entity extends IXingYiResource> IXingYiServerMediaTypeDefn<Entity> jsonAndJavascriptServer(String entityName, MakesFromJson<Entity> makesFromJson, ServerMediaTypeContext<J> context) {
        return new JsonAndJavascriptServerMediaTypeDefn<>(entityName, makesFromJson, context);
    }
    static <J, Entity extends IXingYiResource> IMediaTypeServerDefn<Entity> justJson(String entityName, MakesFromJson<Entity> makesFromJson, JsonParserAndWriter<J> parserAndWriter) {
        return new JustJsonServerMediaTypeDefn<>(makesFromJson, parserAndWriter);
    }
    static <J, Entity extends IXingYiResource> IXingYiServerMediaTypeDefn<Entity> jsonAndLensDefnServer(String entityName, MakesFromJson<Entity> makesFromJson, ServerMediaTypeContext<J> context, List<LensLine> lensLines) {
        return new JsonAndLensDefnServerMediaTypeDefn<>(entityName, makesFromJson, context, lensLines);
    }

}
