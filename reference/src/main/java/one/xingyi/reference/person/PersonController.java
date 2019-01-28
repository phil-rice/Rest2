package one.xingyi.reference.person;

import one.xingyi.core.utils.IdAndValue;
import one.xingyi.core.utils.RunnableWithException;
import one.xingyi.core.utils.WrappedException;
import one.xingyi.reference.address.server.domain.Address;
import one.xingyi.reference.person.server.controller.IPersonController;
import one.xingyi.reference.person.server.domain.Person;
import one.xingyi.reference.telephone.server.domain.TelephoneNumber;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;


public class PersonController implements IPersonController {
    @Override public String stateFn(Person entity) { return ""; }
    final Address address = new Address("someLine1", "someLine2", "somePostcode");
    final TelephoneNumber number = new TelephoneNumber("someNumber");
    final Person person = new Person("someName", 23, address, number);
    Person prototype = new Person("", 0, new Address("", "", ""), new TelephoneNumber(""));

    final Map<String, Person> personStore = Collections.synchronizedMap(new HashMap<>());

    public PersonController() {
        personStore.put("id1", person);
    }
    CompletableFuture<Person> wrap(String id, RunnableWithException runnable) {
        WrappedException.wrap(runnable);
        return CompletableFuture.completedFuture(Optional.ofNullable(personStore.get(id)).orElseThrow(() -> new RuntimeException("Cannot find person with id: " + id)));
    }

    @Override public CompletableFuture<Person> put(IdAndValue<Person> idAndPerson) {
        return wrap(idAndPerson.id, () -> personStore.put(idAndPerson.id, idAndPerson.t));
    }
    @Override public CompletableFuture<Optional<Person>> getOptional(String id) { return CompletableFuture.completedFuture(Optional.ofNullable(personStore.get(id))); }
    @Override public CompletableFuture<Boolean> delete(String id) {
        personStore.remove(id);
        return CompletableFuture.completedFuture(true);
    }
    @Override public CompletableFuture<Person> create(String id) {
        return wrap(id, () -> {
            personStore.put(id, prototype);
        });
    }
    @Override public CompletableFuture<IdAndValue<Person>> create() {
        String id = personStore.size() + "";
        personStore.put(id, prototype);
        return CompletableFuture.completedFuture(new IdAndValue<>(id, prototype));
    }
}
