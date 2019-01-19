package one.xingyi.targetcode.generate.generatedOnServerForClient.person;
import one.xingyi.core.optics.Lens;
import one.xingyi.core.sdk.IXingYiView;
import one.xingyi.targetcode.generate.generatedOnServerForClient.address.IAddressLine12;
import one.xingyi.targetcode.generate.generatedOnServerForShared.person.IPersonEntity;
public interface IPersonAddress12 extends IXingYiView<IPersonEntity> {
    IAddressLine12 address();
    IPersonAddress12 withAddress(IAddressLine12 address);
    Lens<IPersonAddress12, IAddressLine12> addressL = Lens.create(IPersonAddress12::address, IPersonAddress12::withAddress);
}
