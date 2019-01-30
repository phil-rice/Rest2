package one.xingyi.reference4.person;

import one.xingyi.core.store.ControllerUsingMap;
import one.xingyi.core.utils.IdAndValue;
import one.xingyi.core.utils.RunnableWithException;
import one.xingyi.core.utils.WrappedException;
import one.xingyi.reference4.person.server.controller.IPersonController;
import one.xingyi.reference4.person.server.domain.Person;
import one.xingyi.reference4.telephone.server.domain.TelephoneNumber;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;


public class PersonController extends ControllerUsingMap<Person> implements IPersonController {
    @Override public String stateFn(Person entity) { return ""; }
    final TelephoneNumber number = new TelephoneNumber("someNumber");
    final Person person = new Person("someName", number, 23, "someLine1", "someLine2");

    final Map<String, Person> personStore = Collections.synchronizedMap(new HashMap<>());

    public PersonController() {
        super("Person");
        personStore.put("id1", person);
    }
    @Override protected Person prototype(String id) {
        return new Person(id, number, 23, "someLine1", "someLine2");
    }
}
