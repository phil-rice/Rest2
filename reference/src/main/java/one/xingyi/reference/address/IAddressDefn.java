package one.xingyi.reference.address;

import one.xingyi.core.annotations.Entity;
import one.xingyi.core.sdk.IXingYiEntityDefn;

@Entity(bookmark = "/address", getUrl = "<host>/address/<id>")
public interface IAddressDefn extends IXingYiEntityDefn {
    String line1();
    String line2();
    String postcode();
}
