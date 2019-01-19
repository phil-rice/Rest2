package one.xingyi.reference.person;
import one.xingyi.core.sdk.IXingYiOpsDefn;
import one.xingyi.reference.address.IAddressLine12OpsDefn;

public interface IPersonAddress12OpsDefn extends IXingYiOpsDefn<IPersonDefn> {
    IAddressLine12OpsDefn address(); //so this will be the address
}
