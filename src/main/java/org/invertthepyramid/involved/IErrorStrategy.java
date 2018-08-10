package org.invertthepyramid.involved;

import org.invertthepyramid.involved.misc.ApplicationConstant;
import org.invertthepyramid.involved.misc.MDMServiceException;
import org.invertthepyramid.involved.misc.Status;

public interface IErrorStrategy {
    InvolvedException handleMdmException(MDMServiceException mdmec);

    static IErrorStrategy checkConnection(LoggerAdapter log, IAlertReporter alertReporter) { return new CheckConnectionErrorStrategy(log, alertReporter);}
}

class CheckConnectionErrorStrategy implements IErrorStrategy {
    private LoggerAdapter log;
    IAlertReporter alertReporter;

    public CheckConnectionErrorStrategy(LoggerAdapter log, IAlertReporter alertReporter) {
        this.log = log;
        this.alertReporter = alertReporter;
    }

    public InvolvedException handleMdmException(MDMServiceException mdmec) {
        log.error("Exception Message : ", mdmec);
        alertReporter.reportAlert(mdmec);
        return new InvolvedException(ApplicationConstant.ERROR_CODE_0, ApplicationConstant.ERROR_CANNOT_EXECUTE_REQUEST, Status.INTERNAL_SERVER_ERROR);

    }
}
