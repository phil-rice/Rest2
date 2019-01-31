package one.xingyi.core.sdk;
import one.xingyi.core.client.IXingYi;
public interface IXingYiClientFactory<Entity extends IXingYiClientResource, IView extends IXingYiView<Entity>> {
    IView make(IXingYi xingYi, Object mirror);
}
