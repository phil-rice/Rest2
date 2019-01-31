package one.xingyi.accounts;

import one.xingyi.core.annotations.View;
import one.xingyi.core.sdk.IXingYiViewDefn;
@View
public interface IAccountsIdViewDefn extends IXingYiViewDefn<IAccountsDefn> {
    String id();
}
