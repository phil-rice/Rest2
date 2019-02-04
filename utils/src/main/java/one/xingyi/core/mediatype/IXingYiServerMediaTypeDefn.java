package one.xingyi.core.mediatype;
import one.xingyi.core.marshelling.ContextForJson;
import one.xingyi.core.marshelling.DataAndDefn;
import one.xingyi.core.sdk.IXingYiResource;

import java.util.List;
public interface IXingYiServerMediaTypeDefn<Entity extends IXingYiResource> extends IMediaTypeServerDefn<Entity> {
    /** Given an accept header, give me back the list of strings*/
    List<String> lensNames(String acceptHeader);

    /** is this accept header compatible with this media type */
    boolean accept(String acceptHeader);

    DataAndDefn makeDataAndDefn(ContextForJson context, Entity entity);
    @Override default String makeStringFrom(ContextForJson context, Entity entity) {return makeDataAndDefn(context, entity).asString();}
}
