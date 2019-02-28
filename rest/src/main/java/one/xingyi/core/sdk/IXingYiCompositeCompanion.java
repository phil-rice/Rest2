package one.xingyi.core.sdk;
import one.xingyi.core.client.IXingYi;
public interface IXingYiCompositeCompanion<
        Entity extends IXingYiClientResource,
        View extends IXingYiCompositeView<Entity>> extends IXingYiClientMaker<Entity, View> ,IXingYiRemoteAccessDetails<Entity, View>{
}
