package one.xingyi.reference3.person;

import one.xingyi.core.annotations.*;
import one.xingyi.core.sdk.IXingYiEntityDefn;
import one.xingyi.reference3.address.IAddressDefn;
import one.xingyi.reference3.telephone.ITelephoneNumberDefn;

@Entity(bookmark = "/person", rootUrl = "{host}/person/{id}")
@Get(mustExist = false)
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