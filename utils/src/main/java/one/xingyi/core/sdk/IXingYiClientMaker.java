package one.xingyi.core.sdk;
import one.xingyi.core.client.IXingYi;
public interface IXingYiClientMaker<Entity extends IXingYiClientEntity, IView extends IXingYiView<Entity>>  extends IXingYiClientFactory<Entity,IView>{
    Class<IView> opsClass();
    String bookmark();
    String acceptHeader();
}
