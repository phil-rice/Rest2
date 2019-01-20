package one.xingyi.core.client;
import one.xingyi.core.optics.Lens;
import one.xingyi.core.sdk.*;
public interface IXingYi<Entity extends IXingYiClientEntity, View extends IXingYiView<Entity>> {
    Object parse(String s);
    Lens<View, String> stringLens(IXingYiClientFactory<Entity, View> maker, String name);

    <ChildEntity extends IXingYiClientEntity, ChildView extends IXingYiView<ChildEntity>> Lens<View, ChildView> objectLens(IXingYiClientFactory<Entity, View> maker, String name);


}
