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
    public void testRequestChain() {
        RequestChain expected = new RequestChain();
        UpdateAddress updateAddress = new UpdateAddress(mock(IErrorStrategy.class), mock(Service.class), partyAddress, "someLastUpdateUser", new ArrayList<>());
        assertEquals(expected, updateAddress.requestChain());
    }

    @Test
    public void testFromResponseChain() {
        ResponseChain responseChain = new ResponseChain();
        PartyAddress expected = new PartyAddress();
        UpdateAddress updateAddress = new UpdateAddress(mock(IErrorStrategy.class), mock(Service.class), partyAddress, "someLastUpdateUser", new ArrayList<>());
        assertEquals(expected, updateAddress.fromResponseChain(responseChain));
    }
}
