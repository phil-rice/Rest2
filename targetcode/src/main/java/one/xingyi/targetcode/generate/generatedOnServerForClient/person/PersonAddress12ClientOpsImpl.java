package one.xingyi.targetcode.generate.generatedOnServerForClient.person;

import one.xingyi.core.client.IXingYi;
import one.xingyi.core.sdk.IXingYiClientImpl;
import one.xingyi.targetcode.generate.generatedOnServerForClient.address.IAddressLine12;
import one.xingyi.targetcode.generate.generatedOnServerForShared.person.IPersonEntity;

public class PersonAddress12ClientOpsImpl implements IXingYiClientImpl<IPersonEntity, IPersonAddress12>, IPersonAddress12{
    final IXingYi xingYi;
    final Object mirror;
    public PersonAddress12ClientOpsImpl(IXingYi xingYi, Object mirror) {
        this.xingYi = xingYi;
        this.mirror = mirror;
    }
    @Override public IAddressLine12 address() { return null; }
    @Override public IPersonAddress12 withAddress(IAddressLine12 address) { return null; }
}
