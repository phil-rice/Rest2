package one.xingyi.targetcode.generate.generatedOnServerForClient.address;
import one.xingyi.core.client.IXingYi;
import one.xingyi.core.sdk.IXingYiClientOpsCompanion;
import one.xingyi.targetcode.generate.generatedOnServerForShared.address.IAddressEntity;

public class AddressLine12ClientOpsCompanion implements IXingYiClientOpsCompanion<IAddressEntity, IAddressLine12, AddressLine12ClientOpsImpl> {
    @Override public IAddressLine12 create(IXingYi xingYi, Object mirror) {
        return new AddressLine12ClientOpsImpl(xingYi, mirror);
    }
    @Override public Class<IAddressLine12> opsClass() { return IAddressLine12.class; }
    @Override public String bookmark() { return null; }
    @Override public String acceptHeader() { return null; }

}
