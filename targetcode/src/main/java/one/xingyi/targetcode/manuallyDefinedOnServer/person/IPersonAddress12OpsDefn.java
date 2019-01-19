package one.xingyi.targetcode.manuallyDefinedOnServer.person;
import one.xingyi.core.sdk.IXingYiViewDefn;
import one.xingyi.targetcode.manuallyDefinedOnServer.address.IAddressLine12OpsDefn;

public interface IPersonAddress12OpsDefn extends IXingYiViewDefn<IPersonEntityDefn> {
    IAddressLine12OpsDefn address(); //so this will be the address
}
