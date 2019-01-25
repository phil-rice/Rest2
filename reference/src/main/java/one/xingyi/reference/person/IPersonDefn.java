package one.xingyi.reference.person;

import one.xingyi.core.annotations.*;
import one.xingyi.core.sdk.IXingYiEntityDefn;
import one.xingyi.reference.address.IAddressDefn;
import one.xingyi.reference.telephone.ITelephoneNumberDefn;

@Entity(bookmark = "/person", rootUrl = "{host}/person/{id}")
@Get
@Delete
@Put
@CreateWithoutId(url = "{host}/person")
@Create
public interface IPersonDefn extends IXingYiEntityDefn {
    @Field(readOnly = true)
    String name();
    Integer age();
    IAddressDefn address();
    ITelephoneNumberDefn telephone();
}
