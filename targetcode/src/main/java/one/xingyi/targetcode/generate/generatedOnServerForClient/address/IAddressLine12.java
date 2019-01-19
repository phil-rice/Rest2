package one.xingyi.targetcode.generate.generatedOnServerForClient.address;
import one.xingyi.core.optics.Lens;
import one.xingyi.core.sdk.IXingYiView;
import one.xingyi.targetcode.generate.generatedOnServerForShared.address.IAddressEntity;

public interface IAddressLine12 extends IXingYiView<IAddressEntity> {
    String line1();
    IAddressLine12 withLine1(String line1);
    Lens<IAddressEntity, String> line1L = Lens.create(IAddressEntity::line1, IAddressEntity::withLine1);

    String line2();
    IAddressLine12 withLine2(String line2);
    Lens<IAddressEntity, String> line2L = Lens.create(IAddressEntity::line2, IAddressEntity::withLine2);
}
