package one.xingyi.core.mediatype;
import one.xingyi.core.client.IXingYi;
import one.xingyi.reference3.person.client.entitydefn.IPersonNameViewClientEntity;
import one.xingyi.reference3.person.client.view.PersonNameView;
import one.xingyi.reference3.person.server.companion.PersonCompanion;
import one.xingyi.reference3.person.server.domain.Person;
public class JsonAndLensMediaDefnTest extends SimpleMediaTypeDefnClientTests< //TODO Sort out this next
        JsonAndLensDefnServerMediaTypeDefn<Object, Person>,
        JsonAndLensDefnClientMediaTypeDefn<IXingYi<IPersonNameViewClientEntity, PersonNameView>, IPersonNameViewClientEntity, PersonNameView>> {

    @Override protected String makeJsonFromContextAndPerson() { return serverMediaDefn().makeDataAndDefn(contextForJson, person).data; }

    @Override protected one.xingyi.core.mediatype.JsonAndLensDefnServerMediaTypeDefn<Object, Person> serverMediaDefn() {
        return (JsonAndLensDefnServerMediaTypeDefn<Object, Person>) IMediaTypeServerDefn.<Object, Person>jsonAndLensDefnServer(SimpleMediaTypeDefnTest.entityName, PersonCompanion.companion, context, PersonCompanion.companion.lensLines());
    }
    @Override JsonAndLensDefnClientMediaTypeDefn<IXingYi<IPersonNameViewClientEntity, PersonNameView>, IPersonNameViewClientEntity, PersonNameView> clientMediaDefn() {
        return (JsonAndLensDefnClientMediaTypeDefn<IXingYi<IPersonNameViewClientEntity, PersonNameView>, IPersonNameViewClientEntity, PersonNameView>)
                IMediaTypeClientDefn.<IXingYi<IPersonNameViewClientEntity, PersonNameView>, IPersonNameViewClientEntity, PersonNameView>
                        jsonAndLensDefnClient(SimpleMediaTypeDefnTest.entityName, null, null);//todo When we have the javascript working, we can do this with the lens
    }


}
