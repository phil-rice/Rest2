package one.xingyi.reference2.person;

import one.xingyi.core.store.ControllerUsingMap;
import one.xingyi.reference2.address.server.domain.Address;
import one.xingyi.reference2.person.server.controller.IPersonController;
import one.xingyi.reference2.person.server.domain.Person;
import one.xingyi.reference2.telephone.server.domain.TelephoneNumber;


public class PersonController extends ControllerUsingMap<Person> implements IPersonController {
    @Override public String stateFn(Person entity) { return ""; }

    final Address address = new Address("someLine1", "someLine2", "somePostcode");
    final TelephoneNumber number = new TelephoneNumber("someNumber");
    final Person person = new Person("someName", 23, address, number);

    public PersonController() {
        super("person");
        store.put("id1", person);
    }

    @Override protected Person prototype(String id) {
        return new Person(id, 0, new Address("", "", ""), new TelephoneNumber(""));
    }
}
