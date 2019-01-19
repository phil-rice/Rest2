package one.xingyi.reference.person;

import one.xingyi.core.annotations.Entity;
import one.xingyi.core.annotations.Field;
import one.xingyi.core.sdk.IXingYiEntityDefn;
import one.xingyi.reference.address.IAddressDefn;
import one.xingyi.reference.address.domain.IAddress;

@Entity
public interface IPersonDefn extends IXingYiEntityDefn {
    @Field(readOnly = true)
    String name();
    IAddressDefn address();
}
