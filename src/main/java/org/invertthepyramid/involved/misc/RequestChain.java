package org.invertthepyramid.involved.misc;

import org.invertthepyramid.involved.Request;

import java.util.List;

public class RequestChain {
    public RequestChain withRole(List<String> userRoles) {
        return this;
    }

    public RequestChain withRequesterName(String lastUpdateUser) {
        return this;
    }

    public RequestChain withRequest(Request getAllPartyAdminSysKeys) {
        return this;
    }

    public RequestChain withPageStartIndex(String s) {
        return this;
    }

    public RequestChain withPageEndIndex(String s) {
        return this;
    }
}
