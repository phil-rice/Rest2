package one.xingyi.targetcode.generate.generatedOnServerForShared.address;
import one.xingyi.core.sdk.IXingYiEntity;
public interface IAddressEntity extends IXingYiEntity {
    String line1();
    IAddressEntity withLine1(String line1);

    String line2();
    IAddressEntity withLine2(String line2);

    String postcode();
    IAddressEntity withPostcode(String postcode);

}
