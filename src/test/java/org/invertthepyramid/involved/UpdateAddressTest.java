package org.invertthepyramid.involved;

import com.twitter.finagle.Service;
import org.invertthepyramid.involved.domain.PartyAddress;
import org.invertthepyramid.involved.misc.RequestChain;
import org.invertthepyramid.involved.misc.ResponseChain;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class UpdateAddressTest {

    PartyAddress partyAddress = new PartyAddress();

    @Test
    public void testRequestChainIsMadeFromUpdateAddressRequest() {
        RequestChain expected = new RequestChain();
        UpdateAddressRequest request = new UpdateAddressRequest(partyAddress, "someLastUpdateUser", new ArrayList<>());
        assertEquals(expected, request.requestChain());
    }

    @Test
    public void testFromResponseChain() {
        ResponseChain responseChain = new ResponseChain();
        PartyAddress expected = new PartyAddress();
        UpdateAddress updateAddress = new UpdateAddress(mock(IErrorStrategy.class), mock(Service.class));
        assertEquals(expected, updateAddress.fromResponseChain(responseChain));
    }
}
