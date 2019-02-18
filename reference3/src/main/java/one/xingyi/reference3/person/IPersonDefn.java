package one.xingyi.reference3.person;

import one.xingyi.core.annotations.*;
import one.xingyi.core.client.IResourceList;
import one.xingyi.core.sdk.IXingYiResourceDefn;
import one.xingyi.reference3.address.IAddressDefn;
import one.xingyi.reference3.address.IAddressLine12ViewDefn;
import one.xingyi.reference3.telephone.ITelephoneNumberDefn;

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

    IResourceList<IAddressDefn> addresses();
    ITelephoneNumberDefn telephone();

    //==========

    @Deprecated
    @Field(javascript = "return compose(lens_Person_addresses(), lensForFirstItemInList());", lensPath = "addresses/*address,<firstitem>")
    IAddressLine12ViewDefn address();

    @Deprecated
    @Field(javascript = "return compose(lens_Person_address(), lens('line1'));", lensPath = "addresses/*address,<firstitem>,line1/string")
    String line1();

    @Deprecated
    @Field(javascript = "return compose(lens_Person_address(), lens('line2'));", lensPath = "addresses/*address,<firstitem>,line2/string")
    String line2();
}
