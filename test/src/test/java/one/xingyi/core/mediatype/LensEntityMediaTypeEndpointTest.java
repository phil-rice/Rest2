package one.xingyi.core.mediatype;
import one.xingyi.core.endpoints.EndpointConfig;
import one.xingyi.json.Json;
import one.xingyi.reference3.person.server.companion.PersonCompanion;
import one.xingyi.reference3.person.server.domain.Person;

import java.util.List;
public class LensEntityMediaTypeEndpointTest extends EntityMediaTypeEndpointTest<JsonAndJavascriptServerMediaTypeDefn<Object, Person>> {

    @Override protected JsonAndJavascriptServerMediaTypeDefn mediaType() {
        return new JsonAndJavascriptServerMediaTypeDefn("person", PersonCompanion.companion, EndpointConfig.defaultConfig(new Json()).from(List.of(PersonCompanion.companion)));
    }
}
