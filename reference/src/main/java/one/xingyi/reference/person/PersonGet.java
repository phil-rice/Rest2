package one.xingyi.reference.person;

import one.xingyi.core.access.IEntityStore;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.annotations.Get;
import one.xingyi.core.sdk.IXingYiGet;
import one.xingyi.reference.address.domain.Address;
import one.xingyi.reference.person.domain.Person;
import one.xingyi.reference.telephone.domain.TelephoneNumber;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Function;

@Get
public class PersonGet implements IXingYiGet<String, IPersonDefn, Person> {
    public static Address address = new Address("someLine1", "someLine2", "somePostcode");
    public static TelephoneNumber number = new TelephoneNumber("someNumber");
    public static Person person = new Person("someName", 23, address, number);

    IEntityStore<Person> personStore = IEntityStore.map(Map.of("id1", person));

    @Override public BiFunction<ServiceRequest, String,  String> makeId() { return makeIdFromString;}
    @Override public CompletableFuture<Optional<Person>> apply(String s) { return personStore.read(s); }
}
