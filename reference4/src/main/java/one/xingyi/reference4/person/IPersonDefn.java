package one.xingyi.reference4.person;

import one.xingyi.core.annotations.*;
import one.xingyi.core.client.IResourceList;
import one.xingyi.core.sdk.IXingYiResourceDefn;
import one.xingyi.reference4.address.IAddressLine12ViewDefn;
import one.xingyi.reference4.telephone.ITelephoneNumberViewDefn;

@Resource(bookmark = "/person", rootUrl = "{host}/person/{id}")
@OptionalGet
@Get
@Delete
@Put
@CreateWithoutId(url = "{host}/person")
@Create
public interface IPersonDefn extends IXingYiResourceDefn {
    @Field(readOnly = true)
    String name();
    ITelephoneNumberViewDefn telephone();
    Integer age();

    //Basically gone back to model 1 but need to support the legacy
    String line1();
    //Basically gone back to model 1 but need to support the legacy
    String line2();

    //==========

    @Deprecated
    @Field(javascript = "return identityLens();", lensPath = "{identity}")
    IResourceList<IAddressLine12ViewDefn> address();

    @Deprecated
    @Field(javascript = "return itemAsListLens();", lensPath = "{itemAsList}")
    IResourceList<IAddressLine12ViewDefn> addresses();
}
