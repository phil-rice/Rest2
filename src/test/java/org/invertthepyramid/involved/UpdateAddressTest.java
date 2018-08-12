package org.invertthepyramid.involved;

import com.twitter.finagle.Service;
import org.invertthepyramid.involved.domain.PartyAddress;
import org.invertthepyramid.involved.misc.RequestChain;
import org.invertthepyramid.involved.misc.ResponseChain;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class UpdateAddressTest {

    PartyAddress partyAddress = new PartyAddress();

    @Test
    public void testRequestChainIsMadeFromUpdateAddressRequest() {
        RequestChain expected = new RequestChain();
        UpdateAddressRequest request = new UpdateAddressRequest(partyAddress, "someLastUpdateUser", new ArrayList<>());
        assertEquals(expected, UpdateAddressRequest.makeRequestChain.apply(request));
    }

    @Test
    public void testFromResponseChain() {
        ResponseChain responseChain = new ResponseChain();
        PartyAddress expected = new PartyAddress();
        assertEquals(expected, PartyAddress.fromResponseChain.apply(responseChain));
    }

    @Test
    public void testInvolvedImplUpdateAddressServiceIsCreatedCorrectly() {
        AlertReporter reporter = mock(AlertReporter.class);
        LoggerAdapter log = mock(LoggerAdapter.class);
        Service<RequestChain, ResponseChain> mdmService = mock(Service.class);
        InvolvedImpl involved = new InvolvedImpl(log, reporter, mdmService);
        assertEquals(UpdateAddressRequest.makeRequestChain, involved.updateAddress().makeRequestChain);
        assertEquals(PartyAddress.fromResponseChain, involved.updateAddress().fromResponseChain);
        CheckConnectionErrorStrategy strategy = (CheckConnectionErrorStrategy) involved.updateAddress().errorStrategy;
        assertEquals(reporter, strategy.alertReporter);
        assertEquals(log, strategy.log);
        assertEquals(mdmService, involved.updateAddress().mdmService);
    }

    @Test
    public void testInvolvedImplDelegatesToUpdateAddressService() {
        AbstractService service = mock(AbstractService.class);
        InvolvedImpl involved = new InvolvedImpl() {
            @Override
            AbstractService<UpdateAddressRequest, PartyAddress> updateAddress() { return service; }
        };
        List<String> userRoles = mock(List.class);
        involved.updateAddress(partyAddress, "someUser", userRoles);
        verify(service, times(1)).apply(new UpdateAddressRequest(partyAddress, "someUser", userRoles));
    }
}
