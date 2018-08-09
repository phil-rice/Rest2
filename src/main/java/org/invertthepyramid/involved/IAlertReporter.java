package org.invertthepyramid.involved;

import org.invertthepyramid.involved.misc.Alert;
import org.invertthepyramid.involved.misc.EventType;
import org.invertthepyramid.involved.misc.MDMServiceException;
import org.invertthepyramid.involved.misc.Severity;

public interface IAlertReporter {
    void reportAlert(MDMServiceException mdmec);

    static IAlertReporter reporter(LoggerAdapter log, String alertDtapStage) {
        return new AlertReporter(alertDtapStage, log);
    }
}

class AlertDetails {
    static AlertDetails create(MDMServiceException mdmec) {
        if (mdmec.isConnectionReset()) {
            return new AlertDetails(mdmec, EventType.TECHNICAL, Severity.CRITICAL, "IP001");
        } else {
            return new AlertDetails(mdmec, EventType.TECHNICAL, Severity.FATAL, "IP002");
        }

    }

    MDMServiceException mdmec;
    EventType eventType;
    Severity severity;
    String level;

    public AlertDetails(MDMServiceException mdmec, EventType eventType, Severity severity, String level) {
        this.mdmec = mdmec;
        this.eventType = eventType;
        this.severity = severity;
        this.level = level;
    }

    String message(String alertDtapStage) {
        return Alert.builder().eventType(eventType).severity(severity).dtapStage(alertDtapStage).level(level).message(mdmec.getMessage()).build().getLogMessage();
    }
}


class AlertReporter implements IAlertReporter {
    private String alertDtapStage;
    private LoggerAdapter log;

    public AlertReporter(String alertDtapStage, LoggerAdapter log) {
        this.alertDtapStage = alertDtapStage;
        this.log = log;
    }


    public void reportAlert(MDMServiceException mdmec) {
        log.tryError("MDM", () -> AlertDetails.create(mdmec).message(alertDtapStage));
    }


}
