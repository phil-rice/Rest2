package org.invertthepyramid.involved;

public class InvolvedException extends RuntimeException {
    final int errorCode;
    final String errorKind;
    final int status;

    public InvolvedException(int errorCode, String errorKind, int status) {
        this.errorCode = errorCode;
        this.errorKind = errorKind;
        this.status = status;
    }
}
