package one.xingyi.core.mediatype;
import one.xingyi.core.endpoints.EndpointConfig;
import one.xingyi.core.optics.lensLanguage.LensLine;
import one.xingyi.json.Json;
import one.xingyi.reference3.person.server.companion.PersonCompanion;
import one.xingyi.reference3.person.server.domain.Person;

import java.util.List;
public class JavascriptEntityServerMediaTypeEndpointTest extends AbstractEntityServerMediaTypeEndpointTest<JsonAndLensDefnServerMediaTypeDefn<Object, Person>> {

    @Override protected JsonAndLensDefnServerMediaTypeDefn serverMediaType() {
        List<LensLine> lensLens = List.of(new LensLine("someName", List.of()));
        return new JsonAndLensDefnServerMediaTypeDefn("person", PersonCompanion.companion, EndpointConfig.defaultConfig(new Json()).from(List.of(PersonCompanion.companion)), lensLens);
    }
}
