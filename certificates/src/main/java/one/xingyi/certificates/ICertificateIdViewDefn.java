package one.xingyi.certificates;


import one.xingyi.core.annotations.View;
import one.xingyi.core.sdk.IXingYiViewDefn;

@View
@Deprecated
public interface ICertificateIdViewDefn extends IXingYiViewDefn<ICertificateDefn> {
    String id();


}
