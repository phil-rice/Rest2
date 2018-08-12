package org.invertthepyramid.involved.mdm;

import org.invertthepyramid.involved.domain.Severity;

public class Alert {

    private String logMessage= "";
    public static Alert builder() {
        return new Alert();
    }

    public Alert eventType(EventType eventType) {
        return this;
    }

    public Alert severity(Severity severity) {
        return this;
    }

    public Alert dtapStage(String AlertAlertDtapStage) {
        return this;
    }

    public Alert level(String level) {
        return this;
    }

    public Alert message(String message) {
        this.logMessage = message;
        return this;
    }

    public Alert build() {
        return this;
    }

    public String getLogMessage() {
        return logMessage;
    }
}
