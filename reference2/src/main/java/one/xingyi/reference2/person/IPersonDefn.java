package one.xingyi.reference2.person;

import one.xingyi.core.annotations.*;
import one.xingyi.core.sdk.IXingYiResourceDefn;
import one.xingyi.reference2.address.IAddressLine12ViewDefn;
import one.xingyi.reference2.telephone.ITelephoneNumberViewDefn;

@Resource(bookmark = "/person", rootUrl = "{host}/person", urlWithId = "{host}/person/{id}")
@Get @OptionalGet
@Delete
@Put
@CreateWithoutId
@Create
public interface IPersonDefn extends IXingYiResourceDefn {
    @Field(readOnly = true)
    String name();
    Integer age();
    IAddressLine12ViewDefn address();
    ITelephoneNumberViewDefn telephone();

    //==========

    @Deprecated
    @Field(javascript = " return compose(lens_Person_address(), lens('line1'));", lensPath = "address/address,line1/String")
    String line1();

    @Deprecated
    @Field(javascript = " return compose(lens_Person_address(), lens('line2'));", lensPath = "address/address,line1/String")
    String line2();
}
