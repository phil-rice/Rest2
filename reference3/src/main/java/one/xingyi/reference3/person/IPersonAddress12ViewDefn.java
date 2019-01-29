package one.xingyi.reference3.person;
import one.xingyi.core.annotations.View;
import one.xingyi.core.client.ISimpleList;
import one.xingyi.core.sdk.IXingYiViewDefn;
import one.xingyi.reference3.address.IAddressLine12ViewDefn;

import java.util.List;

@View()
public interface IPersonAddress12ViewDefn extends IXingYiViewDefn<IPersonDefn> {
    ISimpleList<IAddressLine12ViewDefn> address(); //so this will be the address
}
