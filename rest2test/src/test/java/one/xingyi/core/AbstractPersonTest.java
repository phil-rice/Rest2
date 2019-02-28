package one.xingyi.core;
import one.xingyi.core.sdk.IXingYiView;
import one.xingyi.reference1.PersonServer;
import one.xingyi.reference1.person.PersonController;
import one.xingyi.reference1.person.client.entitydefn.IPersonClientEntity;
import one.xingyi.reference1.person.server.domain.Person;
import one.xingyi.reference1.telephone.server.domain.TelephoneNumber;
public abstract class AbstractPersonTest<View extends IXingYiView<IPersonClientEntity>> extends AbstractResourceTest<Person, IPersonClientEntity, View, PersonServer<Object>> {

    protected  PersonServer<Object> server() {
        PersonController controller = new PersonController();
        controller.store.put(id(), new Person(id(), 20, startItem(), "someLine2", new TelephoneNumber("someNumber")));
        controller.store.put("prototype", new Person("prototype", 20, "", "", new TelephoneNumber("")));
        return new PersonServer<>(config, controller);
    }

}
