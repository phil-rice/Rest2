package one.xingyi.core.sdk;
public interface IXingYiRemoteAccessDetails <Entity extends IXingYiClientResource, IView extends IXingYiView<Entity>>extends IXingYiClientMaker <Entity,IView>{
    String bookmark();
    String acceptHeader();

}
