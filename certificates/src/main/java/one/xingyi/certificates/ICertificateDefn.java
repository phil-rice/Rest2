package one.xingyi.certificates;

import one.xingyi.core.annotations.*;
import one.xingyi.core.sdk.IXingYiResourceDefn;

@Resource(bookmark = "/certificate", rootUrl = "/certificate/{id}/some/new")
@Delete
@Get(mustExist = false)
@Put
@CreateWithoutId(url = "{host}/certificate")
@Create
public interface ICertificateDefn extends IXingYiResourceDefn {
//        String id();
//}
//
    IDetailsDefn child();

    @Deprecated
    @Field(javascript = "return compose(lens('child'), lens('powerfulId'));")
    String id();
}