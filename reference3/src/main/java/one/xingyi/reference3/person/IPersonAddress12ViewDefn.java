package one.xingyi.reference3.person;
import one.xingyi.core.annotations.View;
import one.xingyi.core.sdk.IXingYiViewDefn;
import one.xingyi.reference3.address.IAddressLine12ViewDefn;

@View()
@Deprecated
public interface IPersonAddress12ViewDefn extends IXingYiViewDefn<IPersonDefn> {
    IAddressLine12ViewDefn address();
}
