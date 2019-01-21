package one.xingyi.core.httpClient;
import one.xingyi.core.annotations.Entity;
import one.xingyi.core.sdk.IXingYiEntityDefn;
@Entity(bookmark = "/entity", getUrl = "/<id>")
public interface IEntityDefn extends IXingYiEntityDefn {
    String urlPattern();
}
