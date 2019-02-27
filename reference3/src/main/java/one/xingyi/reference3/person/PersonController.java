package one.xingyi.reference3.person;

import one.xingyi.core.client.IResourceList;
import one.xingyi.core.store.ControllerUsingMap;
import one.xingyi.reference3.address.server.domain.Address;
import one.xingyi.reference3.person.server.controller.IPersonController;
import one.xingyi.reference3.person.server.domain.Person;
import one.xingyi.reference3.telephone.server.domain.TelephoneNumber;


public class PersonController extends ControllerUsingMap<Person> implements IPersonController {
    @Override public String stateFn(Person entity) { return ""; }

    final Address address = new Address("someLine1", "someLine2", "somePostcode");
    final TelephoneNumber number = new TelephoneNumber("someNumber");
    final Person person = new Person("someName", 23, IResourceList.create(address), number);


    public PersonController() {
        super("Person");
        reset();
    }

    public void reset() {
        store.clear();;
        store.put("id1", person);
    }
    @Override protected Person prototype(String id) {
        return new Person(id, 0, IResourceList.create(), new TelephoneNumber(""));
    }
}
