package one.xingyi.accounts;

import one.xingyi.core.annotations.*;
import one.xingyi.core.sdk.IXingYiResourceDefn;

@Resource(bookmark = "/certificate", rootUrl = "/certificate/{id}/some/new")
@Delete
@Get(mustExist = false)
@Put
@CreateWithoutId(url = "{host}/certificate")
@Create
public interface IAccountsDefn extends IXingYiResourceDefn {
    String id();
}
