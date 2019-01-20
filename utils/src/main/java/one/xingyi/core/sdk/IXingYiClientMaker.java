package one.xingyi.core.sdk;
import one.xingyi.core.client.IXingYi;
public interface IXingYiClientMaker<Entity extends IXingYiEntity, IView extends IXingYiView<Entity>> {
    Class<IView> opsClass();
    String bookmark();
    String acceptHeader();
    IView create(IXingYi xingYi, Object mirror);
}
