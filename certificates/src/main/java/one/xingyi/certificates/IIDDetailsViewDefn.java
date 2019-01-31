package one.xingyi.certificates;


import one.xingyi.certificates.server.domain.IDetails;
import one.xingyi.core.annotations.View;
import one.xingyi.core.sdk.IXingYiViewDefn;

@View
public interface IIDDetailsViewDefn extends IXingYiViewDefn<IDetailsDefn> {
    String powerfulId();
}
