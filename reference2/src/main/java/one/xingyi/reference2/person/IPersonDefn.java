package one.xingyi.reference2.person;

import one.xingyi.core.annotations.*;
import one.xingyi.core.sdk.IXingYiResourceDefn;
import one.xingyi.reference2.address.IAddressDefn;
import one.xingyi.reference2.telephone.ITelephoneNumberDefn;

@Resource(bookmark = "/person", rootUrl = "{host}/person/{id}")
@Get(mustExist = false)
@Delete
@Put
@CreateWithoutId(url = "{host}/person")
@Create
public interface IPersonDefn extends IXingYiResourceDefn {
    @Field(readOnly = true)
    String name();
    Integer age();
    IAddressDefn address();
    ITelephoneNumberDefn telephone();

    //==========

    @Deprecated
    @Field(javascript = " return compose(lens_Person_address(), lens('line1'));")
    String line1();

    @Deprecated
    @Field(javascript = " return compose(lens_Person_address(), lens('line2'));")
    String line2();
}
