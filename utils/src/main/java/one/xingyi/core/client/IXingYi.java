package one.xingyi.core.client;
import one.xingyi.core.optics.Lens;
import one.xingyi.core.sdk.*;
public interface IXingYi {
    Object parse(String s);
    <Entity extends IXingYiClientEntity, View extends IXingYiView<Entity>> Lens<View, String> stringLens(IXingYiClientFactory<Entity, View> maker, String name);

    <Entity extends IXingYiClientEntity, View extends IXingYiView<Entity>,
            ChildEntity extends IXingYiClientEntity, ChildView extends IXingYiView<ChildEntity>>
    Lens<View, ChildView> objectLens(IXingYiClientFactory<Entity, View> maker, String name);


}
