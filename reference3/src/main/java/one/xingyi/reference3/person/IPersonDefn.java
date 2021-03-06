package one.xingyi.reference3.person;

import one.xingyi.core.annotations.*;
import one.xingyi.core.client.IResourceList;
import one.xingyi.core.client.ISimpleList;
import one.xingyi.core.sdk.IXingYiResourceDefn;
import one.xingyi.reference3.address.IAddressLine12ViewDefn;
import one.xingyi.reference3.telephone.ITelephoneNumberViewDefn;

@Resource(bookmark = "/person", rootUrl = "{host}/person", urlWithId = "{host}/person/{id}")
@OptionalGet
@Get
@Delete
@Put
@CreateWithoutId
@Create
@Prototype("prototype")
public interface IPersonDefn extends IXingYiResourceDefn {
    String name();
    Integer age();

    IResourceList<IAddressLine12ViewDefn> addresses();
    ITelephoneNumberViewDefn telephone();

//    ISimpleList <String> friendNames();
    //==========

    @Deprecated
    @Field(javascript = "return compose(lens_Person_addresses(), lensForFirstItemInList());", lensPath = "addresses/*address,{firstItem}")
    IAddressLine12ViewDefn address();

    @Deprecated
    @Field(javascript = "return compose(lens_Person_address(), lens('line1'));", lensPath = "addresses/*address,{firstItem},line1/string")
    String line1();

    @Deprecated
    @Field(javascript = "return compose(lens_Person_address(), lens('line2'));", lensPath = "addresses/*address,{firstItem},line2/string")
    String line2();
}
