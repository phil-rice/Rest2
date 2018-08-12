package org.invertthepyramid.involved.search;

import org.invertthepyramid.involved.domain.IdTypeValue;

import java.util.Date;

public class SearchIndividualRequest {
    public String getLastName() {
        return "someLastName";
    }

    public IdTypeValue getElectronicAddress() {
        return new IdTypeValue("some", "Electron", "ic", "Address");
    }

    public Date getDateOfBirth() {
        return new Date();
    }
}
