package one.xingyi.core.mediatype;
import one.xingyi.core.client.IXingYi;
import one.xingyi.core.client.IXingYiFactory;
import one.xingyi.core.marshelling.FetchJavascript;
import one.xingyi.reference3.person.client.companion.PersonNameViewCompanion;
import one.xingyi.reference3.person.client.entitydefn.IPersonNameViewClientEntity;
import one.xingyi.reference3.person.client.view.PersonNameView;
import one.xingyi.reference3.person.server.companion.PersonCompanion;
import one.xingyi.reference3.person.server.domain.Person;
public class JsonAndJavascriptMediaDefnTest extends SimpleMediaTypeDefnClientTests<
        JsonAndJavascriptServerMediaTypeDefn<Object, Person>,
        JsonAndJavascriptClientMediaTypeDefn<IPersonNameViewClientEntity, PersonNameView>> {


    @Override protected one.xingyi.core.mediatype.JsonAndJavascriptServerMediaTypeDefn<Object, Person> serverMediaDefn() {
        return (JsonAndJavascriptServerMediaTypeDefn<Object, Person>) IMediaTypeServerDefn.<Object, Person>jsonAndJavascriptServer(SimpleMediaTypeDefnTest.entityName, PersonCompanion.companion, context);
    }
    @Override protected String makeJsonFromContextAndPerson() { return serverMediaDefn().makeDataAndDefn(contextForJson, person).data; }
    @Override JsonAndJavascriptClientMediaTypeDefn<IPersonNameViewClientEntity, PersonNameView> clientMediaDefn() {
        return (JsonAndJavascriptClientMediaTypeDefn<IPersonNameViewClientEntity, PersonNameView>)
                IMediaTypeClientDefn.<IPersonNameViewClientEntity, PersonNameView>
                        jsonAndJavascriptClient(SimpleMediaTypeDefnTest.entityName,
                        FetchJavascript.asIs(),
                        IXingYiFactory.simple,
                        PersonNameViewCompanion.companion);
    }
}
