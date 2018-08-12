package org.invertthepyramid.involved.domain;

import org.invertthepyramid.involved.mdm.MakeRequestChain;
import org.invertthepyramid.involved.mdm.RequestChain;

import java.util.List;
import java.util.Objects;

public class UpdateAddressRequest implements MakeRequestChain {
    private PartyAddress partyAddress;
    private String lastUpdateUser;
    private List<String> userRoles;

    public UpdateAddressRequest(PartyAddress partyAddress, String lastUpdateUser, List<String> userRoles) {
        this.partyAddress = partyAddress;
        this.lastUpdateUser = lastUpdateUser;
        this.userRoles = userRoles;
    }

    public RequestChain requestChain() {
        return partyAddress.toCommand().toChain("updatePartyAddress").withRole(userRoles).withRequesterName(lastUpdateUser);
    }

    public static java.util.function.Function<UpdateAddressRequest, RequestChain> makeRequestChain = (rc) -> rc.requestChain();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UpdateAddressRequest that = (UpdateAddressRequest) o;
        return Objects.equals(partyAddress, that.partyAddress) &&
                Objects.equals(lastUpdateUser, that.lastUpdateUser) &&
                Objects.equals(userRoles, that.userRoles);
    }

    @Override
    public int hashCode() {

        return Objects.hash(partyAddress, lastUpdateUser, userRoles);
    }

}
