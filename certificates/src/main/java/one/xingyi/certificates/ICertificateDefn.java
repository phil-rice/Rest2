package one.xingyi.certificates;

import one.xingyi.core.annotations.*;
import one.xingyi.core.sdk.IXingYiResourceDefn;

@Resource(bookmark = "/certificate", urlWithId = "{host}/certificate/{id}")
@Delete @Get @OptionalGet @Put @Create
public interface ICertificateDefn extends IXingYiResourceDefn {
    IIDDetailsViewDefn child();

    @Deprecated
    @Field(lensPath = "child/details,powerfulId/String")
    String id();

}
