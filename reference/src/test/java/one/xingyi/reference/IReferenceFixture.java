package one.xingyi.reference;
import one.xingyi.reference.address.domain.Address;
import one.xingyi.reference.person.domain.Person;
public interface IReferenceFixture {
    Address address = new Address("someLine1","someLine2", "somePostcode");
    Person person = new Person("someName",address);
}
