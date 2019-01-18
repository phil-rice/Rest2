package one.xingyi.reference.person;

import one.xingyi.core.annotations.Entity;
import one.xingyi.core.annotations.Field;
import one.xingyi.core.sdk.IXingYiEntityDefn;
import one.xingyi.reference.address.IAddressEntityDefn;

@Entity
public interface IPersonEntityDefn extends IXingYiEntityDefn {
    @Field(readOnly = true)
    String name();
    IAddressEntityDefn address();
}
