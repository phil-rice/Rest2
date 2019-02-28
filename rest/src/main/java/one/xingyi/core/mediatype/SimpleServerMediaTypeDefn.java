package one.xingyi.core.mediatype;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.javascript.JavascriptDetailsToString;
import one.xingyi.core.javascript.JavascriptStore;
import one.xingyi.core.marshelling.*;
import one.xingyi.core.optics.lensLanguage.LensLine;
import one.xingyi.core.sdk.IXingYiResource;
import one.xingyi.core.utils.IdAndValue;
import one.xingyi.core.utils.Lists;
import one.xingyi.core.utils.Strings;

import java.util.List;
import java.util.function.Function;
abstract class SimpleServerMediaTypeDefn<J, Entity extends IXingYiResource & HasJsonWithLinks<ContextForJson, Entity>> implements IXingYiServerMediaTypeDefn<Entity> {
    final String prefix;
    final MakesFromJson<Entity> makesFromJson;
    final JsonParserAndWriter<J> parserAndWriter;

    public SimpleServerMediaTypeDefn(MakesFromJson<Entity> makesFromJson, JsonParserAndWriter<J> parserAndWriter, String prefix, String entityName) {
        this.makesFromJson = makesFromJson;
        this.parserAndWriter = parserAndWriter;
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
    @Override public Entity makeEntityFrom(String acceptHeader, String string) { return makesFromJson.fromJson(parserAndWriter, parserAndWriter.parse(string)); }
    @Override public boolean accept(String acceptHeader) { return acceptHeader.toLowerCase().startsWith(prefix); }

    @Override public DataToBeSentToClient makeDataAndDefn(ContextForJson context, Function<Entity, String> stateFn, Entity entity) {
        return makeDataAndDefnFor(context, entity.toJsonWithLinks(parserAndWriter, context, stateFn));
    }
    @Override public DataToBeSentToClient makeDataAndDefn(ContextForJson context, Function<Entity, String> stateFn, IdAndValue<Entity> idAndValue) {
        return makeDataAndDefnFor(context, IdAndValue.toJson(idAndValue, parserAndWriter, context, stateFn));
    }

    abstract DataToBeSentToClient makeDataAndDefnFor(ContextForJson context, J json);


}


class JsonAndJavascriptServerMediaTypeDefn<J, Entity extends IXingYiResource & HasJsonWithLinks<ContextForJson, Entity>> extends SimpleServerMediaTypeDefn<J, Entity> implements IXingYiServerMediaTypeDefn<Entity> {
    final JavascriptStore javascriptStore;
    final JavascriptDetailsToString javascriptDetailsToString;
    final String protocol;


    public JsonAndJavascriptServerMediaTypeDefn(String entityName, MakesFromJson<Entity> makesFromJson, ServerMediaTypeContext context) {
        super(makesFromJson, context.parserAndWriter(), IMediaTypeConstants.jsonJavascriptPrefix, entityName);
        this.javascriptStore = context.javascriptStore();
        this.javascriptDetailsToString = context.javascriptDetailsToString();
        this.protocol = context.protocol();
    }

    DataToBeSentToClient makeDataAndDefnFor(ContextForJson context, J json) {
        String data = parserAndWriter.asString(json);
        String defn = javascriptDetailsToString.apply(javascriptStore.find(lensNames(context.acceptHeader())));
        return new DataToBeSentToClient(data, defn);
    }
    @Override public ContextForJson makeContextForJson(ServiceRequest serviceRequest) {
        return ContextForJson.forServiceRequest(protocol, serviceRequest);
    }
}
