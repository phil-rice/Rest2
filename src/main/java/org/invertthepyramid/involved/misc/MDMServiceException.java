package org.invertthepyramid.involved.misc;

public class MDMServiceException extends RuntimeException {

    private int errorId;
    private String throwableMessage;

    public MDMServiceException(int errorId, String throwableMessage) {
        super(throwableMessage);
        this.errorId = errorId;
        this.throwableMessage = throwableMessage;
    }

    public String getThrowableMessage() {
        return throwableMessage;
    }

    public int getErrorId() {
        return errorId;
    }

    public boolean isConnectionReset() {
        return getErrorId() == 0 && getMessage() != null && getMessage().contains("Connection reset by peer at remote address");
    }
}
