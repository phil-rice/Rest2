package one.xingyi.reference1.person;

import one.xingyi.core.store.ControllerUsingMap;
import one.xingyi.reference1.person.server.controller.IPersonController;
import one.xingyi.reference1.person.server.domain.Person;
import one.xingyi.reference1.telephone.server.domain.TelephoneNumber;


public class PersonController extends ControllerUsingMap<Person> implements IPersonController {
    final TelephoneNumber number = new TelephoneNumber("someNumber");
    final Person person = new Person("someName", 23, "someLine1", "someLine2", number);

    public PersonController() {
        super("person");
        store.put("id1", person);
    }

    @Override protected Person prototype(String id) {
        return new Person(id, 0, "", "", new TelephoneNumber(""));
    }

    @Override public String stateFn(Person entity) { return ""; }
}
