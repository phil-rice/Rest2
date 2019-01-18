package one.xingyi.targetcode.manuallyDefinedOnServer.address;

import one.xingyi.core.sdk.IXingYiEntityDefn;

public interface IAddressEntityDefn extends IXingYiEntityDefn {
    String line1();
    String line2();
    String postcode();
}
