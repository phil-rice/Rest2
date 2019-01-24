package one.xingyi.reference.person;
import one.xingyi.core.annotations.View;
import one.xingyi.core.sdk.IXingYiViewDefn;
import one.xingyi.reference.address.IAddressLine12ViewDefn;

@View()
public interface IPersonAddress12ViewDefn extends IXingYiViewDefn<IPersonDefn> {
    IAddressLine12ViewDefn address(); //so this will be the address
}
