package one.xingyi.reference.person;
import one.xingyi.core.sdk.IXingYiOpsDefn;
import one.xingyi.reference.address.IAddressLine12OpsDefn;
import one.xingyi.reference.person.domain.IPerson;

public interface IPersonAddress12OpsDefn extends IXingYiOpsDefn<IPerson> {
    IAddressLine12OpsDefn address(); //so this will be the address
}
