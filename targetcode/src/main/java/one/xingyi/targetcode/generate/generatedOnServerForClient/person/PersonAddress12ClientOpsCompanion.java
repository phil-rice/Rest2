package one.xingyi.targetcode.generate.generatedOnServerForClient.person;

import one.xingyi.core.client.IXingYi;
import one.xingyi.core.sdk.IXingYiClientOpsCompanion;
import one.xingyi.targetcode.generate.generatedOnServerForShared.person.IPersonEntity;

public class PersonAddress12ClientOpsCompanion implements IXingYiClientOpsCompanion<IPersonEntity, IPersonAddress12, PersonAddress12ClientOpsImpl> {
    @Override public IPersonAddress12 create(IXingYi xingYi, Object mirror) {
        return new PersonAddress12ClientOpsImpl(xingYi, mirror);
    }
    @Override public Class<IPersonAddress12> opsClass() { return IPersonAddress12.class; }
    @Override public String bookmark() { return null; }
    @Override public String acceptHeader() { return null; }
}
