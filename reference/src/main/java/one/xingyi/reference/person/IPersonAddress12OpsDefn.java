package one.xingyi.reference.person;
import one.xingyi.core.sdk.IXingYiViewDefn;
import one.xingyi.reference.address.IAddressLine12OpsDefn;
import one.xingyi.reference.person.domain.IPerson;

public interface IPersonAddress12OpsDefn extends IXingYiViewDefn<IPersonDefn> {
    IAddressLine12OpsDefn address(); //so this will be the address
}
