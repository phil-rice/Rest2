package one.xingyi.core.sdk;
import one.xingyi.core.client.IXingYi;
public interface IXingYiClientMaker<Entity extends IXingYiEntity, IOps extends IXingYiOps<Entity>> {
    Class<IOps> opsClass();
    String bookmark();
    String acceptHeader();
    IOps create(IXingYi xingYi, Object mirror);
}
