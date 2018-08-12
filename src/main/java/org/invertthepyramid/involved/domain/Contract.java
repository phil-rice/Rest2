package org.invertthepyramid.involved.domain;

import org.invertthepyramid.involved.mdm.CommonBObjType;
import org.invertthepyramid.involved.search.SearchContract;

public class Contract {
    public static SearchContract searchContract(String contractType, String contractId) {
        return new SearchContract();
    }

    public static Contract fromResponse(CommonBObjType object) {
        return new Contract();
    }
}
