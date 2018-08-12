package org.invertthepyramid.involved.utilities;

import org.invertthepyramid.involved.InvolvedException;
import org.invertthepyramid.involved.domain.Status;
import org.invertthepyramid.involved.mdm.ApplicationConstant;
import org.invertthepyramid.involved.mdm.MDMServiceException;

public class CheckConnectionErrorStrategy implements IErrorStrategy {
    public final LoggerAdapter log;
    public final IAlertReporter alertReporter;

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
