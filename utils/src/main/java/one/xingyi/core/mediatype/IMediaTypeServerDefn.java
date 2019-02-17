package one.xingyi.core.mediatype;
import one.xingyi.core.endpoints.*;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.marshelling.*;
import one.xingyi.core.optics.lensLanguage.LensLine;
import one.xingyi.core.sdk.IXingYiResource;
import one.xingyi.core.utils.IdAndValue;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
public interface IMediaTypeServerDefn<Entity extends IXingYiResource> {

    /**
     * This parses the string and returns an entity. It can throw exceptions if the string is malformed
     */
    Entity makeEntityFrom(String acceptHeader, String string);

    ContextForJson makeContextForJson(ServiceRequest serviceRequest);

    DataToBeSentToClient makeDataAndDefn(ContextForJson context, Function<Entity, String> stateFn, Entity entity);
    DataToBeSentToClient makeDataAndDefn(ContextForJson context, Function<Entity, String> stateFn, IdAndValue<Entity> entity);

    default IResourceEndpoints<Entity> endpoints(String protocol, BookmarkCodeAndUrlPattern bookmarkCodeAndUrlPattern, Function<Entity, String> stateFn) { return new MediaTypeResourceEndpoints<>(protocol, this, bookmarkCodeAndUrlPattern, stateFn); }

    static <J, Entity extends IXingYiResource & HasJsonWithLinks<ContextForJson, Entity>> IXingYiServerMediaTypeDefn<Entity> jsonAndJavascriptServer(String entityName, MakesFromJson<Entity> makesFromJson, ServerMediaTypeContext<J> context) {
        return new JsonAndJavascriptServerMediaTypeDefn<>(entityName, makesFromJson, context);
    }
    static <J, Entity extends IXingYiResource& HasJsonWithLinks<ContextForJson, Entity>> IMediaTypeServerDefn<Entity> justJson(String protocol, MakesFromJson<Entity> makesFromJson, JsonParserAndWriter<J> parserAndWriter) {
        return new JustJsonServerMediaTypeDefn<>(protocol, makesFromJson, parserAndWriter);
    }
    static <J, Entity extends IXingYiResource& HasJsonWithLinks<ContextForJson, Entity>> IXingYiServerMediaTypeDefn<Entity> jsonAndLensDefnServer(String entityName, MakesFromJson<Entity> makesFromJson, ServerMediaTypeContext<J> context, List<LensLine> lensLines) {
        return new JsonAndLensDefnServerMediaTypeDefn<>(entityName, makesFromJson, context, lensLines);
    }

}
