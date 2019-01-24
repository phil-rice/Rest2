package one.xingyi.reference;
import one.xingyi.reference.address.server.domain.Address;
import one.xingyi.reference.person.server.domain.Person;
import one.xingyi.reference.telephone.server.domain.TelephoneNumber;
public interface IReferenceFixture {
    Address address = new Address("someLine1","someLine2", "somePostcode");
    TelephoneNumber number = new TelephoneNumber("someNumber");
    Person person = new Person("someName",23,address, number);
}
