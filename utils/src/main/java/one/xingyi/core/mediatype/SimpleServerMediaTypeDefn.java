package one.xingyi.core.mediatype;
import one.xingyi.core.javascript.JavascriptDetailsToString;
import one.xingyi.core.javascript.JavascriptStore;
import one.xingyi.core.marshelling.ContextForJson;
import one.xingyi.core.marshelling.DataAndDefn;
import one.xingyi.core.marshelling.JsonParserAndWriter;
import one.xingyi.core.marshelling.MakesFromJson;
import one.xingyi.core.optics.lensLanguage.LensLine;
import one.xingyi.core.sdk.IXingYiResource;
import one.xingyi.core.utils.Lists;
import one.xingyi.core.utils.Strings;

import java.util.List;
import java.util.function.Function;
abstract class SimpleServerMediaTypeDefn<Entity extends IXingYiResource> implements IXingYiServerMediaTypeDefn<Entity> {
    final String prefix;

    public SimpleServerMediaTypeDefn(String prefix, String entityName) {
        this.prefix = (prefix + "." + entityName).toLowerCase();
    }
    @Override public List<String> lensNames(String acceptHeader) {
        String lowerCaseAcceptHeader = acceptHeader.toLowerCase();
        if (lowerCaseAcceptHeader.startsWith(prefix)) {
            String withoutPrefix = lowerCaseAcceptHeader.substring(prefix.length());
            if (withoutPrefix.length() == 0) return List.of();
            return Strings.split(withoutPrefix.substring(1), "\\.");
        }
        throw new RuntimeException("Illegal access of media type. Must start with " + prefix + " but was " + lowerCaseAcceptHeader);
    }
    @Override public boolean accept(String acceptHeader) {
        return acceptHeader.toLowerCase().startsWith(prefix);
    }
}


class JsonAndJavascriptServerMediaTypeDefn<J, Entity extends IXingYiResource> extends SimpleServerMediaTypeDefn<Entity> {
    final MakesFromJson<Entity> makesFromJson;
    final JsonParserAndWriter<J> parserAndWriter;
    final JavascriptStore javascriptStore;
    final JavascriptDetailsToString javascriptDetailsToString;


    public JsonAndJavascriptServerMediaTypeDefn(String entityName, MakesFromJson<Entity> makesFromJson, ServerMediaTypeContext context) {
        super(IMediaTypeConstants.jsonJavascriptPrefix, entityName);
        this.makesFromJson = makesFromJson;
        this.parserAndWriter = context.parserAndWriter();
        this.javascriptStore = context.javascriptStore();
        this.javascriptDetailsToString = context.javascriptDetailsToString();
    }
    @Override public Entity makeEntityFrom(String acceptHeader, String string) {
        return makesFromJson.fromJson(parserAndWriter, parserAndWriter.parse(string));
    }
    @Override public DataAndDefn makeDataAndDefn(ContextForJson context, Function<String, String> entityEnvelopeFn, Function<Entity, String> stateFn, Entity o) {
        String data = entityEnvelopeFn.apply(o.toJsonString(parserAndWriter, context));
        String acceptHeader = context.acceptHeader();
        String defn = javascriptDetailsToString.apply(javascriptStore.find(lensNames(acceptHeader)));
        return new DataAndDefn(data, defn);
    }
}
class JsonAndLensDefnServerMediaTypeDefn<J, Entity extends IXingYiResource> extends SimpleServerMediaTypeDefn<Entity> {
    final MakesFromJson<Entity> makesFromJson;
    final JsonParserAndWriter<J> parserAndWriter;
    private List<LensLine> lensLines;
    public JsonAndLensDefnServerMediaTypeDefn(String entityName, MakesFromJson<Entity> makesFromJson, ServerMediaTypeContext<J> context, List<LensLine> lensLines) {
        super(IMediaTypeConstants.jsonDefnPrefix, entityName);
        this.makesFromJson = makesFromJson;
        this.parserAndWriter = context.parserAndWriter();
        this.lensLines = lensLines;
    }

    @Override public DataAndDefn makeDataAndDefn(ContextForJson context, Function<String, String> entityEnvelopeFn, Function<Entity, String> stateFn, Entity entity) {
        String json = entityEnvelopeFn.apply(entity.toJsonString(parserAndWriter, context));
        String lensDefnString = Lists.mapJoin(lensLines, "\n", LensLine::asString);
        return new DataAndDefn(json, lensDefnString);
    }
    @Override public Entity makeEntityFrom(String acceptHeader, String string) {
        return makesFromJson.fromJson(parserAndWriter, parserAndWriter.parse(string));
    }
}
