package one.xingyi.core.client;
import one.xingyi.core.optics.Lens;
import one.xingyi.core.sdk.*;
public interface IXingYi {
    Object parse(String s);
    <Entity extends IXingYiClientEntity, View extends IXingYiView<Entity>> Lens<View,String> stringLens(IXingYiClientFactory<Entity, View> maker, String name);

//    <T extends IXingYiClientImpl> Lens<T, String> stringLens(IDomainMaker<T> domainMaker, String name);
//    <T1 extends XingYiDomain, T2> Lens<T1, T2> objectLens(IDomainMaker<T1> domainMaker1, IDomainMaker<T2> domainMaker2, String name);

}
