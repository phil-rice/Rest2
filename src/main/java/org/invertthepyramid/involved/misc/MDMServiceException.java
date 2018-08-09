package org.invertthepyramid.involved.misc;

public class MDMServiceException extends RuntimeException {

    public String getThrowableMessage() {
        return "some error message";
    }

    public int getErrorId() {
        return 0;
    }
}
