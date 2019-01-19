package one.xingyi.targetcode.generate.generatedOnServerForClient.person;
import one.xingyi.core.sdk.IXingYiView;
import one.xingyi.targetcode.generate.generatedOnServerForShared.person.IPersonEntity;
public interface IPersonName extends IXingYiView<IPersonEntity> {
    String name();
    IPersonEntity withName(String name);
}
