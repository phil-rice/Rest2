package one.xingyi.targetcode.generate.generatedOnServerForServerDevelopers.address;
import one.xingyi.targetcode.generate.generatedOnServerForShared.address.IAddressEntity;
public class Address implements IAddressEntity {
    final String line1;
    final String line2;
    final String postcode;

    public Address(String line1, String line2, String postcode) {
        this.line1 = line1;
        this.line2 = line2;
        this.postcode = postcode;
    }

    @Override public String line1() {return line1; }
    @Override public IAddressEntity withLine1(String line1) { return null; }
    ;
    @Override public String line2() {return line2; }
    @Override public IAddressEntity withLine2(String line2) { return null; }

    @Override public String postcode() {return postcode; }
    @Override public IAddressEntity withPostcode(String postcode) { return null; }


}
