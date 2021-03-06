package one.xingyi.core.mediatype;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.marshelling.ContextForJson;
import one.xingyi.core.marshelling.DataToBeSentToClient;
import one.xingyi.core.marshelling.HasJsonWithLinks;
import one.xingyi.core.marshelling.MakesFromJson;
import one.xingyi.core.optics.lensLanguage.LensLine;
import one.xingyi.core.sdk.IXingYiResource;
import one.xingyi.core.utils.Lists;

import java.util.List;
public class JsonAndLensDefnServerMediaTypeDefn<J, Entity extends IXingYiResource & HasJsonWithLinks<ContextForJson, Entity>> extends SimpleServerMediaTypeDefn<J, Entity> {
    final String protocol;
    final List<String> lensLines;

    public JsonAndLensDefnServerMediaTypeDefn(String entityName, MakesFromJson<Entity> makesFromJson, ServerMediaTypeContext<J> context, List<String> lensLines) {
        super(makesFromJson, context.parserAndWriter(), IMediaTypeConstants.jsonDefnPrefix, entityName);
        this.lensLines = lensLines;
        this.protocol = context.protocol();
    }

    DataToBeSentToClient makeDataAndDefnFor(ContextForJson context, J json) {
        String data = parserAndWriter.fromJ(json);
        String lensDefnString = Lists.join(lensLines, "\n");
        return new DataToBeSentToClient(data, lensDefnString);
    }

    @Override public ContextForJson makeContextForJson(ServiceRequest serviceRequest) { return ContextForJson.forServiceRequest(protocol, serviceRequest); }
}
