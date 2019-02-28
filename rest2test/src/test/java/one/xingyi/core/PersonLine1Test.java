package one.xingyi.core;
import one.xingyi.core.sdk.IXingYiRemoteAccessDetails;
import one.xingyi.reference1.person.client.entitydefn.IPersonClientEntity;
import one.xingyi.reference1.person.client.view.PersonLine12View;
import one.xingyi.reference1.person.client.viewcompanion.PersonLine12ViewCompanion;

public class PersonLine1Test extends AbstractPersonTest<PersonLine12View> {
    @Override protected IXingYiRemoteAccessDetails<IPersonClientEntity, PersonLine12View> accessDetails() { return PersonLine12ViewCompanion.companion; }
    @Override protected String getItem(PersonLine12View view) { return view.line1(); }
    @Override protected PersonLine12View withItem(PersonLine12View view, String item) { return view.withline1(item); }
}
