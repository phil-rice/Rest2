package org.invertthepyramid.involved;

import com.twitter.finagle.Service;
import org.invertthepyramid.involved.domain.PartyAddress;
import org.invertthepyramid.involved.domain.UpdateAddressRequest;
import org.invertthepyramid.involved.mdm.MdmService;
import org.invertthepyramid.involved.mdm.RequestChain;
import org.invertthepyramid.involved.mdm.ResponseChain;
import org.invertthepyramid.involved.utilities.CheckConnectionErrorStrategy;
import org.invertthepyramid.involved.utilities.IAlertReporter;
import org.invertthepyramid.involved.utilities.LoggerAdapter;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class InvolvedImplTest {
    PartyAddress partyAddress = new PartyAddress();

    @Test
    public void testInvolvedImplUpdateAddressServiceIsCreatedCorrectly() {
        IAlertReporter reporter = mock(IAlertReporter.class);
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
        MdmService service = mock(MdmService.class);
        InvolvedImpl involved = new InvolvedImpl() {
            @Override
            MdmService<UpdateAddressRequest, PartyAddress> updateAddress() { return service; }
        };
        List<String> userRoles = mock(List.class);
        involved.updateAddress(partyAddress, "someUser", userRoles);
        verify(service, times(1)).apply(new UpdateAddressRequest(partyAddress, "someUser", userRoles));
    }

}
