package one.xingyi.targetcode.generate.generatedOnClientForClientCode;
import one.xingyi.core.client.IXingYi;
import one.xingyi.core.sdk.IXingYiCompositeImpl;
import one.xingyi.targetcode.generate.generatedOnServerForClient.address.IAddressLine12;
import one.xingyi.targetcode.generate.generatedOnServerForClient.person.IPersonAddress12;
import one.xingyi.targetcode.generate.generatedOnServerForShared.person.IPersonEntity;
import one.xingyi.targetcode.manuallyDefinedOnClient.IPersonNameLine12;

public class PersonNameL12ClientCompositeImpl implements IXingYiCompositeImpl<IPersonEntity, IPersonNameLine12>, IPersonNameLine12 {
    final IXingYi xingYi;
    final Object mirror;

    public PersonNameL12ClientCompositeImpl(IXingYi xingYi, Object mirror) {
        this.xingYi = xingYi;
        this.mirror = mirror;
    }
    @Override public IAddressLine12 address() { return null; }
    @Override public IPersonAddress12 withAddress(IAddressLine12 address) { return null; }
    @Override public String name() { return null; }
    @Override public IPersonEntity withName(String name) { return null; }
}
