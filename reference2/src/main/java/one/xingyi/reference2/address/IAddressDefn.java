package one.xingyi.reference2.address;

import one.xingyi.core.annotations.Entity;
import one.xingyi.core.sdk.IXingYiEntityDefn;

//@Entity(bookmark = "/address", rootUrl = "{host}/address/{id}")
@Entity
public interface IAddressDefn extends IXingYiEntityDefn {
    String line1();
    String line2();
    String postcode();
}
