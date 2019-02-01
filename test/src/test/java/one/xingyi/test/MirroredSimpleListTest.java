package one.xingyi.test;
import one.xingyi.core.client.ISimpleList;
import one.xingyi.core.client.IXingYi;
import one.xingyi.core.client.IXingYiFactory;
import one.xingyi.core.client.MirroredSimpleList;
import one.xingyi.core.endpoints.EndpointConfig;
import one.xingyi.core.endpoints.EndpointContext;
import one.xingyi.core.marshelling.ContextForJson;
import one.xingyi.core.marshelling.JsonValue;
import one.xingyi.core.sdk.IXingYiClientResource;
import one.xingyi.core.sdk.IXingYiView;
import one.xingyi.json.Json;
import one.xingyi.reference3.PersonServer;
import one.xingyi.reference3.address.client.view.AddressLine12View;
import one.xingyi.reference3.address.client.view.AddressLine12ViewImpl;
import one.xingyi.reference3.address.server.companion.AddressCompanion;
import one.xingyi.reference3.person.client.entitydefn.IPersonAddresses12ViewClientEntity;
import one.xingyi.reference3.person.client.entitydefn.IPersonNameViewClientEntity;
import one.xingyi.reference3.person.client.view.PersonAddresses12View;
import one.xingyi.reference3.person.client.view.PersonAddresses12ViewImpl;
import one.xingyi.reference3.person.server.companion.PersonCompanion;
import one.xingyi.reference3.person.server.domain.Person;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;
public class MirroredSimpleListTest implements IReferenceFixture3 {
    EndpointConfig<Object> config = EndpointConfig.defaultConfig(new Json(), new Json());
    EndpointContext<Object> context = config.from(List.of(PersonCompanion.companion, AddressCompanion.companion));

    IXingYiFactory factory = IXingYiFactory.simple;
    IXingYi xingyi = factory.apply(context.javascriptDetailsToString.apply(context.javascriptStore.find(List.of())));
    String personJson = config.jsonWriter.fromJ(IReferenceFixture3.person.toJson(config.jsonWriter, ContextForJson.nullContext));
    PersonAddresses12ViewImpl view = new PersonAddresses12ViewImpl(xingyi, xingyi.parse(personJson));

    @Test
    public void testWithOneAddress() {
        assertEquals(1, view.addresses().size());
        assertEquals("someLine1", view.addresses().get(0).line1());
    }

    @Test public void testAddAnAddress() {
        AddressLine12View prototype = view.addresses().get(0);
        AddressLine12View line2 = prototype.withline1("someLineFor2");
        ISimpleList<AddressLine12View> with2 = view.addresses().append(line2);

        assertEquals(1, view.addresses().size());
        assertEquals("someLine1", view.addresses().get(0).line1());

        assertEquals(2, with2.size());
        assertEquals("someLine1", with2.get(0).line1());
        assertEquals("someLineFor2", with2.get(1).line1());
    }


}