package one.xingyi.reference.person;

import one.xingyi.core.annotations.View;
import one.xingyi.core.sdk.IXingYiOpsDefn;
@View
public interface IPersonNameOpsDefn extends IXingYiOpsDefn<IPersonDefn> {
    String name();
}
