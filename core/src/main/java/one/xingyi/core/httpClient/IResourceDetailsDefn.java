package one.xingyi.core.httpClient;
import one.xingyi.core.annotations.Resource;
import one.xingyi.core.annotations.Field;
import one.xingyi.core.annotations.Get;
import one.xingyi.core.sdk.IXingYiResourceDefn;
@Get(mustExist = false)
@Resource(bookmark = "/resource", rootUrl = "{host}/{id}")
public interface IResourceDetailsDefn extends IXingYiResourceDefn {
    @Field(templated = true)
    String urlPattern();
}
