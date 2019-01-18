package one.xingyi.targetcode.generate.generatedOnServerForClient.person;
import one.xingyi.core.sdk.IXingYiOps;
import one.xingyi.targetcode.generate.generatedOnServerForShared.person.IPersonEntity;
public interface IPersonName extends IXingYiOps<IPersonEntity> {
    String name();
    IPersonEntity withName(String name);
}
