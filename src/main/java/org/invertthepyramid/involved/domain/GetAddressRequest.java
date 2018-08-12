package org.invertthepyramid.involved.domain;

import org.invertthepyramid.involved.mdm.RequestChain;

import java.util.List;

public class GetAddressRequest {
    String partyAddressIdPK;
    List<String> userRoles;

    public GetAddressRequest(String partyAddressIdPK, List<String> userRoles) {
        this.partyAddressIdPK = partyAddressIdPK;
        this.userRoles = userRoles;
    }

    RequestChain requestChain() {
        return PartyAddress.get(partyAddressIdPK).toChain("getPartyAddressByIdPK").withRole(userRoles);
    }

    public static java.util.function.Function<GetAddressRequest, RequestChain> makeRequestChain = (r) -> r.requestChain();
}
