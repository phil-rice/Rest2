package one.xingyi.certificates;

import one.xingyi.core.annotations.*;
import one.xingyi.core.sdk.IXingYiEntityDefn;

@Entity(bookmark = "/certificate", rootUrl = "/certificate/{id}")
@Delete
@Get(mustExist = false)
@Put
@CreateWithoutId(url = "{host}/certificate")
@Create
public interface ICertificateDefn extends IXingYiEntityDefn {
//
   IDetailsDefn child();

    @Deprecated
    @Field(javascript = "return compose(lens('child'), lens('powerfulId'));")
    String id();
}
