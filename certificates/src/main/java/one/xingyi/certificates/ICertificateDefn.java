package one.xingyi.certificates;

import one.xingyi.core.annotations.*;
import one.xingyi.core.sdk.IXingYiResourceDefn;

@Resource(bookmark = "/certificate", rootUrl = "{host}/certificate/{id}")
@Delete
@Get(mustExist = false)
@Put
//@CreateWithoutId(url = "{host}/certificate")
@Create
public interface ICertificateDefn extends IXingYiResourceDefn {
    IDetailsDefn child();

    @Deprecated
    @Field(javascript = "return compose(lens('child'), lens('powerfulId'));")
    String id();

//        @Deprecated
//        @Field(defn = "return lens('newNew')")
//        String id();
//

}
