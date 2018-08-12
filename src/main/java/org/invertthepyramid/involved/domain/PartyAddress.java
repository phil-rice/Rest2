package org.invertthepyramid.involved.domain;

import org.invertthepyramid.involved.misc.Command;
import org.invertthepyramid.involved.misc.CommonBObjType;
import org.invertthepyramid.involved.misc.Function;
import org.invertthepyramid.involved.misc.ResponseChain;

public class PartyAddress {
    //This is just a 'scaffolding implementation' it lets us compile and test the main code and that's all

    public static PartyAddress fromResponse(CommonBObjType commonBObjType) {
        return new PartyAddress();
    }

    public static java.util.function.Function<ResponseChain, PartyAddress> fromResponseChain =
            (responseChain) -> PartyAddress.fromResponse(responseChain.getSafeResponse(0).getObject(0));

    public static Command get(String partyAddressIdPK) {
        return new Command();
    }

    public static PartyAddress fromResponse(CommonBObjType object, boolean b) {
        return new PartyAddress();
    }

    public Command toCommand() {
        return new Command();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof PartyAddress;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
