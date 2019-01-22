package one.xingyi.reference.person;

import one.xingyi.core.annotations.Entity;
import one.xingyi.core.annotations.Field;
import one.xingyi.core.sdk.IXingYiEntityDefn;
import one.xingyi.reference.address.IAddressDefn;
import one.xingyi.reference.telephone.ITelephoneNumberDefn;

@Entity(bookmark = "/person", getUrl = "<host>/person/<id>")
public interface IPersonDefn extends IXingYiEntityDefn {
    @Field(readOnly = true)
    String name();
    Integer age();
    IAddressDefn address();
    ITelephoneNumberDefn telephone();
}
