package one.xingyi.core.sdk;
import one.xingyi.core.client.IXingYi;
public interface IXingYiClientFactory<Entity extends IXingYiClientEntity, IView extends IXingYiView<Entity>> {
    //TODO it would be good to make this type safe.
    IView create(IXingYi xingYi, Object mirror);
}
