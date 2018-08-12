package org.invertthepyramid.involved.search;

import org.invertthepyramid.involved.domain.GetPartyContactMethodByIdPK;

public class SearchContract {
    public void setInquiryLevel(int inquiryLevel1) {
    }

    public GetPartyContactMethodByIdPK toCommand() {
        return new GetPartyContactMethodByIdPK();
    }
}
