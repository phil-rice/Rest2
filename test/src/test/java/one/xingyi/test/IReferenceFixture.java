package one.xingyi.test;
import one.xingyi.reference3.address.server.domain.Address;
import one.xingyi.reference3.person.server.domain.Person;
import one.xingyi.reference3.telephone.server.domain.TelephoneNumber;
public interface IReferenceFixture {
    Address address = new Address("someLine1","someLine2", "somePostcode");
    TelephoneNumber number = new TelephoneNumber("someNumber");
    Person person = new Person("someName",23,address, number);
}
