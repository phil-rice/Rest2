package one.xingyi.targetcode.generate.generatedOnClientForClientCode;
import one.xingyi.core.client.IXingYi;
import one.xingyi.core.sdk.IXingYiCompositeCompanion;
import one.xingyi.targetcode.generate.generatedOnServerForShared.person.IPersonEntity;
import one.xingyi.targetcode.manuallyDefinedOnClient.IPersonNameLine12;

public class PersonNameL12ClientCompositeCompanion implements IXingYiCompositeCompanion<IPersonEntity, IPersonNameLine12, PersonNameL12ClientCompositeImpl> {
    @Override public IPersonNameLine12 create(IXingYi xingYi, Object mirror) {
        return new PersonNameL12ClientCompositeImpl(xingYi, mirror);
    }
    @Override public Class<IPersonNameLine12> opsClass() { return IPersonNameLine12.class; }
    @Override public String bookmark() { return null; }
    @Override public String acceptHeader() { return null; }
}
