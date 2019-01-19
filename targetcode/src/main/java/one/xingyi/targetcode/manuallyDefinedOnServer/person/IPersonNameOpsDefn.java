package one.xingyi.targetcode.manuallyDefinedOnServer.person;

import one.xingyi.core.sdk.IXingYiOpsDefn;
import one.xingyi.targetcode.generate.generatedOnServerForShared.person.IPersonEntity;
public interface IPersonNameOpsDefn extends IXingYiOpsDefn<IPersonEntity> {
    String name();
}
