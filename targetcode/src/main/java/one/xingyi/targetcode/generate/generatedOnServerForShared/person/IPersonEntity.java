package one.xingyi.targetcode.generate.generatedOnServerForShared.person;
import one.xingyi.core.sdk.IXingYiEntity;
import one.xingyi.targetcode.manuallyDefinedOnServer.address.IAddressEntityDefn;
public interface IPersonEntity extends IXingYiEntity {
    String name();
    IPersonEntity withName(String name);

    IAddressEntityDefn address();
    IPersonEntity withAddress(String address);
}
