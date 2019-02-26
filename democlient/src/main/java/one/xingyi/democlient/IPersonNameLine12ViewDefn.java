package one.xingyi.democlient;
import one.xingyi.core.annotations.CombinedView;
import one.xingyi.core.sdk.IXingYiCompositeDefn;
import one.xingyi.reference1.person.client.entitydefn.IPersonClientEntity;
import one.xingyi.reference1.person.client.view.PersonLine12View;
import one.xingyi.reference1.person.client.view.PersonNameView;

@CombinedView
public interface IPersonNameLine12ViewDefn extends IXingYiCompositeDefn<IPersonClientEntity>, PersonLine12View, PersonNameView {
}
