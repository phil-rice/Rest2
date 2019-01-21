package one.xingyi.reference;
import one.xingyi.reference.address.domain.Address;
import one.xingyi.reference.person.domain.Person;
import one.xingyi.reference.telephone.domain.TelephoneNumber;
public interface IReferenceFixture {
    Address address = new Address("someLine1","someLine2", "somePostcode");
    TelephoneNumber number = new TelephoneNumber("someNumber");
    Person person = new Person("someName",23,address, number);
}
