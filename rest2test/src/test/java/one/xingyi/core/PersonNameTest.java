package one.xingyi.core;
import one.xingyi.core.sdk.IXingYiRemoteAccessDetails;
import one.xingyi.reference1.person.client.entitydefn.IPersonClientEntity;
import one.xingyi.reference1.person.client.view.PersonLine12View;
import one.xingyi.reference1.person.client.view.PersonNameView;
import one.xingyi.reference1.person.client.viewcompanion.PersonLine12ViewCompanion;
import one.xingyi.reference1.person.client.viewcompanion.PersonNameViewCompanion;

public class PersonNameTest extends AbstractPersonTest<PersonNameView> {
    @Override protected String startItem() { return "someId"; }
    @Override protected IXingYiRemoteAccessDetails<IPersonClientEntity, PersonNameView> accessDetails() { return PersonNameViewCompanion.companion; }
    @Override protected String getItem(PersonNameView view) { return view.name(); }
    @Override protected PersonNameView withItem(PersonNameView view, String item) { return view.withname(item); }
}
