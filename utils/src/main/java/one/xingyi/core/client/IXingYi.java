package one.xingyi.core.client;
import one.xingyi.core.sdk.*;
public interface IXingYi {
    Object parse(String s);
    <Entity extends IXingYiEntity, View extends IXingYiView<Entity>> View stringLens(IXingYiClientMaker<Entity, View> maker);

//    <T extends IXingYiClientImpl> Lens<T, String> stringLens(IDomainMaker<T> domainMaker, String name);
//    <T1 extends XingYiDomain, T2> Lens<T1, T2> objectLens(IDomainMaker<T1> domainMaker1, IDomainMaker<T2> domainMaker2, String name);

}
