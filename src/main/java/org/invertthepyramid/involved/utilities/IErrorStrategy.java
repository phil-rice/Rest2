package org.invertthepyramid.involved.utilities;

import org.invertthepyramid.involved.InvolvedException;
import org.invertthepyramid.involved.mdm.ApplicationConstant;
import org.invertthepyramid.involved.mdm.MDMServiceException;
import org.invertthepyramid.involved.domain.Status;

public interface IErrorStrategy {
    InvolvedException handleMdmException(MDMServiceException mdmec);

    static IErrorStrategy checkConnection(LoggerAdapter log, IAlertReporter alertReporter) { return new CheckConnectionErrorStrategy(log, alertReporter);}
}

