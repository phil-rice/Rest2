package one.xingyi.core.mediatype;

import one.xingyi.core.endpoints.EndpointConfig;
import one.xingyi.core.marshelling.FetchJavascript;
import one.xingyi.core.optics.lensLanguage.LensStoreParser;
import one.xingyi.json.Json;
import one.xingyi.reference3.person.client.companion.PersonNameViewCompanion;
import one.xingyi.reference3.person.client.entitydefn.IPersonClientEntity;
import one.xingyi.reference3.person.client.view.PersonNameView;
import one.xingyi.reference3.person.server.companion.PersonCompanion;
import one.xingyi.reference3.person.server.domain.Person;

import java.util.List;

public class LensEntityServerMediaTypeEndpointTest extends AbstractEntityClientMediaTypeEndpointTest
        <JsonAndLensDefnServerMediaTypeDefn<Object, Person>, JsonAndLensDefnClientMediaTypeDefn<Object, IPersonClientEntity, PersonNameView>> {

    @Override
    protected JsonAndLensDefnServerMediaTypeDefn<Object, Person> serverMediaType() {
        return new JsonAndLensDefnServerMediaTypeDefn<Object, Person>("person", PersonCompanion.companion, EndpointConfig.defaultConfig(new Json()).from(List.of(PersonCompanion.companion)), PersonCompanion.companion.lensLines());
    }

    @Override
    JsonAndLensDefnClientMediaTypeDefn<Object, IPersonClientEntity, PersonNameView> clientMediaType() {
        return new JsonAndLensDefnClientMediaTypeDefn<Object, IPersonClientEntity, PersonNameView>("person", new Json(), FetchJavascript.asIs(), LensStoreParser.simple(), PersonNameViewCompanion.companion);
    }
}
