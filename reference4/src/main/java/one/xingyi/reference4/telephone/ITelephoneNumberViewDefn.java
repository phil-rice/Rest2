package one.xingyi.reference4.telephone;
import one.xingyi.core.annotations.View;
import one.xingyi.core.sdk.IXingYiViewDefn;

@View
public interface ITelephoneNumberViewDefn extends IXingYiViewDefn<ITelephoneNumberDefn> {
    String number();
}
