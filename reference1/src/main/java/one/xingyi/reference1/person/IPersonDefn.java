package one.xingyi.reference1.person;

import one.xingyi.core.annotations.*;
import one.xingyi.core.client.IResourceList;
import one.xingyi.core.sdk.IXingYiResourceDefn;
import one.xingyi.reference1.telephone.ITelephoneNumberViewDefn;

@Resource(bookmark = "/person", urlWithId = "{host}/person/{id}")
@Get @OptionalGet @Delete @Put @Create
@CreateWithoutId(url = "{host}/person")
@Prototype("prototype")
public interface IPersonDefn extends IXingYiResourceDefn {
    String name();
    Integer age();
    String line1();
    String line2();
    ITelephoneNumberViewDefn telephone();
}
