package one.xingyi.core.httpClient;
import one.xingyi.core.annotations.Entity;
import one.xingyi.core.annotations.Field;
import one.xingyi.core.sdk.IXingYiEntityDefn;
@Entity(bookmark = "/entity", rootUrl = "{host}/{id}")
public interface IEntityDetailsDefn extends IXingYiEntityDefn {
    @Field(templated = true)
    String urlPattern();
}
