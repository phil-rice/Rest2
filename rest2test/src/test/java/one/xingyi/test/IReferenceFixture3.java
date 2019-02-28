package one.xingyi.test;
import one.xingyi.core.client.IResourceList;
import one.xingyi.reference3.address.server.domain.Address;
import one.xingyi.reference3.person.server.domain.Person;
import one.xingyi.reference3.telephone.server.domain.TelephoneNumber;
public interface IReferenceFixture3 {
    Address address1 = new Address("someLine1a","someLine2a", "somePostcode1");
    Address address2 = new Address("someLine1b","someLine2b", "somePostcode2");
    TelephoneNumber number = new TelephoneNumber("someNumber");
    Person person = new Person("someName",23, IResourceList.create(address1,address2), number);
}
