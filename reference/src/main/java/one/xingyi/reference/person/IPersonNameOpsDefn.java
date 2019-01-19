package one.xingyi.reference.person;

import one.xingyi.core.annotations.View;
import one.xingyi.core.sdk.IXingYiOpsDefn;
import one.xingyi.reference.person.domain.IPerson;
@View
public interface IPersonNameOpsDefn extends IXingYiOpsDefn<IPerson> {
    String name();
    IPersonDefn person();
}
