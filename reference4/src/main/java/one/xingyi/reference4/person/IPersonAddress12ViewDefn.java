package one.xingyi.reference4.person;
import one.xingyi.core.annotations.View;
import one.xingyi.core.sdk.IXingYiViewDefn;
import one.xingyi.reference4.address.IAddressLine12ViewDefn;

@View()
@Deprecated
public interface IPersonAddress12ViewDefn extends IXingYiViewDefn<IPersonDefn> {
    IAddressLine12ViewDefn address();
}
