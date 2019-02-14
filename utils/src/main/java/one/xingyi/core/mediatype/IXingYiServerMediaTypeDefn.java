package one.xingyi.core.mediatype;
import one.xingyi.core.marshelling.ContextForJson;
import one.xingyi.core.marshelling.DataAndDefn;
import one.xingyi.core.sdk.IXingYiResource;
import one.xingyi.core.utils.IdAndValue;

import java.util.List;
import java.util.function.Function;
public interface IXingYiServerMediaTypeDefn<Entity extends IXingYiResource> extends IMediaTypeServerDefn<Entity> {
    /** Given an accept header, give me back the list of strings*/
    List<String> lensNames(String acceptHeader);

    /** is this accept header compatible with this media type */
    boolean accept(String acceptHeader);

    DataAndDefn makeDataAndDefn(ContextForJson context, Function<Entity, String> stateFn, Entity entity);
    DataAndDefn makeDataAndDefn(ContextForJson context, Function<Entity, String> stateFn, IdAndValue<Entity> entity);

    @Override default String makeStringFrom(ContextForJson context, Function<String, String> entityEnvelopeFn, Function<Entity, String> stateFn, Entity entity) {
        return makeDataAndDefn(context, stateFn, entity).asString();
    }
}
