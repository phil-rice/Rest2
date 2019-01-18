package one.xingyi.targetcode.manuallyDefinedOnServer.person;
import one.xingyi.core.sdk.IXingYiOpsDefn;
import one.xingyi.targetcode.manuallyDefinedOnServer.address.IAddressLine12OpsDefn;

public interface IPersonAddress12OpsDefn extends IXingYiOpsDefn<IPersonEntityDefn> {
    IAddressLine12OpsDefn address(); //so this will be the address
}
