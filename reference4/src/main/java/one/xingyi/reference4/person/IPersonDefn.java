package one.xingyi.reference4.person;

import one.xingyi.core.annotations.*;
import one.xingyi.core.client.ISimpleList;
import one.xingyi.core.sdk.IXingYiResourceDefn;
import one.xingyi.reference4.address.IAddressDefn;
import one.xingyi.reference4.address.IAddressLine12ViewDefn;
import one.xingyi.reference4.telephone.ITelephoneNumberDefn;

@Resource(bookmark = "/person", rootUrl = "{host}/person/{id}")
@Get(mustExist = false)
@Delete
@Put
@CreateWithoutId(url = "{host}/person")
@Create
public interface IPersonDefn extends IXingYiResourceDefn {
    @Field(readOnly = true)
    String name();
    ITelephoneNumberDefn telephone();
    Integer age();

    //Basically gone back to model 1 but need to support the legacy
    String line1();
    //Basically gone back to model 1 but need to support the legacy
    String line2();

    //==========

    @Deprecated
    @Field(javascript = "return identityLens();")
    ISimpleList<IAddressLine12ViewDefn> address();

    @Deprecated
    @Field(javascript = "return itemAsListLens();")
    ISimpleList<IAddressDefn> addresses();
}
