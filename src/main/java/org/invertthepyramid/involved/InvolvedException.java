package org.invertthepyramid.involved;

public class InvolvedException extends RuntimeException {
    public final int errorCode;
    public final String errorKind;
    public final int status;

    public InvolvedException(int errorCode, String errorKind, int status) {
        this.errorCode = errorCode;
        this.errorKind = errorKind;
        this.status = status;
    }
}
