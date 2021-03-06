package one.xingyi.reference3.person;

import lombok.RequiredArgsConstructor;
import one.xingyi.core.client.IResourceList;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.marshelling.JsonParser;
import one.xingyi.core.marshelling.JsonParserAndWriter;
import one.xingyi.core.store.ControllerUsingMap;
import one.xingyi.reference3.address.server.domain.Address;
import one.xingyi.reference3.person.server.companion.PersonCompanion;
import one.xingyi.reference3.person.server.controller.IPersonController;
import one.xingyi.reference3.person.server.domain.Person;
import one.xingyi.reference3.telephone.server.domain.TelephoneNumber;



public class PersonController <J>extends ControllerUsingMap<Person> implements IPersonController {
    final JsonParser parser;

    @Override public String stateFn(Person entity) { return ""; }

    final Address address = new Address("someLine1", "someLine2", "somePostcode");
    final TelephoneNumber number = new TelephoneNumber("someNumber");
    final Person person = new Person("someName", 23, IResourceList.create(address), number);


    public PersonController(JsonParser<J> parser) {
        super("Person");
        this.parser = parser;
        reset();
    }

    public void reset() {
        store.clear(); ;
        store.put("id1", person);
        this.store.put("prototype", prototype("prototype"));
    }
    @Override protected Person prototype(String id) {
        return new Person(id, 0, IResourceList.create(new Address("", "", "")), new TelephoneNumber(""));
    }

    @Override public Person createWithoutIdRequestFrom(ServiceRequest serviceRequest) {
        return PersonCompanion.companion.fromJson(parser, parser.parse(serviceRequest.body));
    }

}
