package one.xingyi.core.mediatype;
import one.xingyi.core.javascript.JavascriptDetailsToString;
import one.xingyi.core.javascript.JavascriptStore;
import one.xingyi.core.marshelling.ContextForJson;
import one.xingyi.core.marshelling.DataAndDefn;
import one.xingyi.core.marshelling.JsonParserAndWriter;
import one.xingyi.core.marshelling.MakesFromJson;
import one.xingyi.core.sdk.IXingYiResource;
import one.xingyi.core.utils.Strings;

import java.util.List;
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


abstract class JsonServerMediaTypeDefn<J, Entity extends IXingYiResource> extends SimpleServerMediaTypeDefn<Entity> {
    final MakesFromJson<Entity> makesFromJson;
    final JsonParserAndWriter<J> parserAndWriter;
    final JavascriptStore javascriptStore;
    final JavascriptDetailsToString javascriptDetailsToString;


    public JsonServerMediaTypeDefn(String prefix, String entityName, MakesFromJson<Entity> makesFromJson, ServerMediaTypeContext context) {
        super(prefix, entityName);
        this.makesFromJson = makesFromJson;
        this.parserAndWriter = context.parserAndWriter();
        this.javascriptStore = context.javascriptStore();
        this.javascriptDetailsToString = context.javascriptDetailsToString();
    }
    @Override public Entity makeEntityFrom(String acceptHeader, String string) {
        return makesFromJson.fromJson(parserAndWriter, parserAndWriter.parse(string));
    }
    @Override public DataAndDefn makeDataAndDefn(ContextForJson context, Entity o) {
        String data = o.toJsonString(parserAndWriter, context);
        String defn = javascriptDetailsToString.apply(javascriptStore.find(lensNames(context.acceptHeader())));
        return new DataAndDefn(data, defn);
    }
}
class JsonAndJavascriptServerMediaTypeDefn<J, Entity extends IXingYiResource> extends JsonServerMediaTypeDefn<J, Entity> {
    public JsonAndJavascriptServerMediaTypeDefn(String entityName, MakesFromJson<Entity> makesFromJson, ServerMediaTypeContext<J> context) {
        super(IMediaTypeConstants.jsonJavascriptPrefix, entityName, makesFromJson, context);
    }
}
class JsonAndLensDefnServerMediaTypeDefn<J, Entity extends IXingYiResource> extends JsonServerMediaTypeDefn<J, Entity> {
    public JsonAndLensDefnServerMediaTypeDefn(String entityName, MakesFromJson<Entity> makesFromJson, ServerMediaTypeContext<J> context) {
        super(IMediaTypeConstants.jsonDefnPrefix, entityName, makesFromJson, context);
    }
}
