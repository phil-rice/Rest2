package one.xingyi.core.mediatype;
import one.xingyi.json.Json;
import one.xingyi.reference3.person.server.companion.PersonCompanion;
import one.xingyi.reference3.person.server.domain.Person;

import java.util.function.Function;
public class JustJsonMediaDefnTest extends SimpleMediaTypeDefnTest<JustJsonServerMediaTypeDefn<Object, Person>> {


    @Override protected JustJsonServerMediaTypeDefn<Object, Person> serverMediaDefn() {
        return (JustJsonServerMediaTypeDefn<Object, Person>) IMediaTypeServerDefn.<Object, Person>justJson("http", PersonCompanion.companion, new Json());
    }
    @Override protected String acceptHeader() { return ""; }
    @Override protected String makeJsonFromContextAndPerson() { return serverMediaDefn().makeDataAndDefn(contextForJson, e -> "", person).data; }

}
