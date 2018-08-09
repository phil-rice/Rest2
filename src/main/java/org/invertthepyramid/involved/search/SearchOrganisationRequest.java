package org.invertthepyramid.involved.search;

import org.invertthepyramid.involved.domain.IdTypeValue;

public class SearchOrganisationRequest {


    public String getName() {
        return "someName";
    }

    public IdTypeValue getExternalIdentifier() {
        return new IdTypeValue("id", "type", "name", "value");
    }
}
