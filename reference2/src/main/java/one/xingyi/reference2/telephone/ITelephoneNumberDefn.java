package one.xingyi.reference2.telephone;
import one.xingyi.core.annotations.Entity;
import one.xingyi.core.sdk.IXingYiEntityDefn;
@Entity
public interface ITelephoneNumberDefn extends IXingYiEntityDefn {
    String number();
}