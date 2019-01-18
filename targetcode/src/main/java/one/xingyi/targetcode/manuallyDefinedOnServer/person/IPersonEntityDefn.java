package one.xingyi.targetcode.manuallyDefinedOnServer.person;

import one.xingyi.core.sdk.IXingYiEntityDefn;
import one.xingyi.targetcode.manuallyDefinedOnServer.address.IAddressEntityDefn;

public interface IPersonEntityDefn extends IXingYiEntityDefn {
    String name();
    IAddressEntityDefn address();
}
