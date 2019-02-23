package one.xingyi.core.mediatype;
import one.xingyi.core.client.IXingYiFactory;
import one.xingyi.core.marshelling.FetchJavascript;
import one.xingyi.reference3.person.client.entitydefn.IPersonClientEntity;
import one.xingyi.reference3.person.client.view.PersonNameView;
import one.xingyi.reference3.person.client.viewcompanion.PersonNameViewCompanion;
import one.xingyi.reference3.person.server.companion.PersonCompanion;
import one.xingyi.reference3.person.server.domain.Person;
public class JsonAndJavascriptMediaDefnTest extends SimpleMediaTypeDefnClientTests<Object,
        JsonAndJavascriptServerMediaTypeDefn<Object, Person>,
        JsonAndJavascriptClientMediaTypeDefn<IPersonClientEntity, PersonNameView>> {


    @Override protected one.xingyi.core.mediatype.JsonAndJavascriptServerMediaTypeDefn<Object, Person> serverMediaDefn() {
        return (JsonAndJavascriptServerMediaTypeDefn<Object, Person>) IMediaTypeServerDefn.<Object, Person>jsonAndJavascriptServer(SimpleMediaTypeDefnTest.entityName, PersonCompanion.companion, context);
    }
    @Override protected String makeJsonFromContextAndPerson() { return serverMediaDefn().makeDataAndDefn(contextForJson, p -> "", person).data; }
    @Override JsonAndJavascriptClientMediaTypeDefn<IPersonClientEntity, PersonNameView> clientMediaDefn() {
        return (JsonAndJavascriptClientMediaTypeDefn<IPersonClientEntity, PersonNameView>)
                IMediaTypeClientDefn.<IPersonClientEntity, PersonNameView>
                        jsonAndJavascriptClient(SimpleMediaTypeDefnTest.entityName,
                        FetchJavascript.asIs(),
                        IXingYiFactory.simple,
                        PersonNameViewCompanion.companion);
    }
}
