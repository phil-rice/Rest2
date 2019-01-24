package one.xingyi.reference.address;

import one.xingyi.core.access.IEntityStore;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.sdk.IXingYiGet;
import one.xingyi.reference.address.server.domain.Address;
import one.xingyi.reference.person.PersonGet;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;

public class AddressGet implements IXingYiGet<String, IAddressDefn, Address> {
    static IEntityStore<Address> addressStore = IEntityStore.map(Map.of("add1", PersonGet.address));

    @Override public BiFunction<ServiceRequest, String, String> makeId() { return makeIdFromString;}
    @Override public CompletableFuture<Optional<Address>> apply(String s) { return addressStore.read(s); }
}
