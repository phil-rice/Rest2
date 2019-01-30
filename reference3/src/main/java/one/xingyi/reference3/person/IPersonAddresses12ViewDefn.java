package one.xingyi.reference3.person;
import one.xingyi.core.annotations.View;
import one.xingyi.core.client.ISimpleList;
import one.xingyi.core.sdk.IXingYiViewDefn;
import one.xingyi.reference3.address.IAddressLine12ViewDefn;

@View()
public interface IPersonAddresses12ViewDefn extends IXingYiViewDefn<IPersonDefn> {
    ISimpleList<IAddressLine12ViewDefn> addresses();
}
