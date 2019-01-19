package one.xingyi.reference.address;

import one.xingyi.core.annotations.Entity;
import one.xingyi.core.sdk.IXingYiEntityDefn;

@Entity
public interface IAddress extends IXingYiEntityDefn {
    String line1();
    String line2();
    String postcode();
}
