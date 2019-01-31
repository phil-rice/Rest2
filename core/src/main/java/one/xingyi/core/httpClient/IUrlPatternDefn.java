package one.xingyi.core.httpClient;
import one.xingyi.core.annotations.View;
import one.xingyi.core.sdk.IXingYiViewDefn;
@View
public interface IUrlPatternDefn extends IXingYiViewDefn<IResourceDetailsDefn> {
    String urlPattern();
}
