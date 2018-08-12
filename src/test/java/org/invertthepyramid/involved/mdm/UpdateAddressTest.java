package org.invertthepyramid.involved.mdm;

import com.twitter.finagle.Service;
import org.invertthepyramid.involved.InvolvedImpl;
import org.invertthepyramid.involved.domain.PartyAddress;
import org.invertthepyramid.involved.domain.UpdateAddressRequest;
import org.invertthepyramid.involved.utilities.CheckConnectionErrorStrategy;
import org.invertthepyramid.involved.utilities.IAlertReporter;
import org.invertthepyramid.involved.utilities.LoggerAdapter;
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

}
