package one.xingyi.reference4.address;

import one.xingyi.core.annotations.Resource;
import one.xingyi.core.sdk.IXingYiResourceDefn;

//@Entity(bookmark = "/address", urlWithId = "{host}/address/{id}")
@Resource
@Deprecated
public interface IAddressDefn extends IXingYiResourceDefn {
    String line1();
    String line2();
    String postcode();
}
