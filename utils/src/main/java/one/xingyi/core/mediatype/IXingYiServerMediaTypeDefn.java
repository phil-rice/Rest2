package one.xingyi.core.mediatype;
import one.xingyi.core.sdk.IXingYiResource;

import java.util.List;
public interface IXingYiServerMediaTypeDefn<Entity extends IXingYiResource> extends IMediaTypeServerDefn<Entity> {
    /** Given an accept header, give me back the list of strings*/
    List<String> lensNames(String acceptHeader);

    /** is this accept header compatible with this media type */
    boolean accept(String acceptHeader);


//    @Override default String makeStringFrom(ContextForJson context, Function<String, String> entityEnvelopeFn, Function<Entity, String> stateFn, Entity entity) {
//        return makeDataAndDefn(context, stateFn, entity).asString();
//    }
}
