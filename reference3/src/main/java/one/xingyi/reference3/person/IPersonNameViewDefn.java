package one.xingyi.reference3.person;

import one.xingyi.core.annotations.View;
import one.xingyi.core.sdk.IXingYiViewDefn;
@View
public interface IPersonNameViewDefn extends IXingYiViewDefn<IPersonDefn> {
    String name();
 }
