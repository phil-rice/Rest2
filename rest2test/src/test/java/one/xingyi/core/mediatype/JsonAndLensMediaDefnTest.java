package one.xingyi.core.mediatype;
import one.xingyi.core.marshelling.FetchJavascript;
import one.xingyi.json.Json;
import one.xingyi.reference3.person.client.entitydefn.IPersonClientEntity;
import one.xingyi.reference3.person.client.view.PersonNameView;
import one.xingyi.reference3.person.client.viewcompanion.PersonNameViewCompanion;
import one.xingyi.reference3.person.server.companion.PersonCompanion;
import one.xingyi.reference3.person.server.domain.Person;
public class JsonAndLensMediaDefnTest extends SimpleMediaTypeDefnClientTests<Object,
        JsonAndLensDefnServerMediaTypeDefn<Object, Person>,
        JsonAndLensDefnClientMediaTypeDefn<Object, IPersonClientEntity, PersonNameView>> {

    @Override protected String makeJsonFromContextAndPerson() { return serverMediaDefn().makeDataAndDefn(contextForJson, p -> "", person).data; }

    @Override protected one.xingyi.core.mediatype.JsonAndLensDefnServerMediaTypeDefn<Object, Person> serverMediaDefn() {
        return (JsonAndLensDefnServerMediaTypeDefn<Object, Person>) IMediaTypeServerDefn.<Object, Person>jsonAndLensDefnServer(SimpleMediaTypeDefnTest.entityName, PersonCompanion.companion, context, PersonCompanion.companion.lensLines());
    }
    @Override JsonAndLensDefnClientMediaTypeDefn<Object, IPersonClientEntity, PersonNameView> clientMediaDefn() {
        return (JsonAndLensDefnClientMediaTypeDefn<Object, IPersonClientEntity, PersonNameView>)
                IMediaTypeClientDefn.<Object, IPersonClientEntity, PersonNameView>
                        jsonAndLensDefnClient(SimpleMediaTypeDefnTest.entityName, new Json(), FetchJavascript.asIs(), PersonNameViewCompanion.companion);
    }


}
