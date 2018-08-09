package org.invertthepyramid.involved.search;

import org.invertthepyramid.involved.misc.GetPartyContactMethodByIdPK;

public class SearchContract {
    public void setInquiryLevel(int inquiryLevel1) {
    }

    public GetPartyContactMethodByIdPK toCommand() {
        return new GetPartyContactMethodByIdPK();
    }
}
