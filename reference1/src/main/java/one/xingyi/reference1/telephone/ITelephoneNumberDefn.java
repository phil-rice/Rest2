package one.xingyi.reference1.telephone;
import one.xingyi.core.annotations.Resource;
import one.xingyi.core.sdk.IXingYiResourceDefn;
@Resource
public interface ITelephoneNumberDefn extends IXingYiResourceDefn {
    String number();
}
