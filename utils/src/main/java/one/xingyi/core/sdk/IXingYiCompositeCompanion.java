package one.xingyi.core.sdk;
import one.xingyi.core.client.IXingYi;
public interface IXingYiCompositeCompanion<Entity extends IXingYiClientEntity, IOps extends IXingYiCompositeDefn<Entity>, Impl extends IXingYiCompositeImpl<Entity, IOps>> extends IXingYiClientMaker<Entity, IOps> {
    IOps create(IXingYi xingYi, Object mirror);
    String acceptHeader();
}
