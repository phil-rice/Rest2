package org.invertthepyramid.involved.utilities;

import org.invertthepyramid.involved.InvolvedException;
import org.invertthepyramid.involved.mdm.ApplicationConstant;
import org.invertthepyramid.involved.mdm.MDMServiceException;
import org.invertthepyramid.involved.domain.Status;
import org.invertthepyramid.involved.utilities.IAlertReporter;
import org.invertthepyramid.involved.utilities.IErrorStrategy;
import org.invertthepyramid.involved.utilities.LoggerAdapter;
import org.junit.Test;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class CheckConnectionErrorStrategyTest {

    MDMServiceException e = mock(MDMServiceException.class);

    @Test
    public void testLogs() {
        LoggerAdapter log = mock(LoggerAdapter.class);
        IAlertReporter reporter = mock(IAlertReporter.class);

        IErrorStrategy.checkConnection(log, reporter).handleMdmException(e);

        verify(log, times(1)).error("Exception Message : ", e);
    }

    @Test
    public void testReturnsAnInvolvedException() {
        LoggerAdapter log = mock(LoggerAdapter.class);
        IAlertReporter reporter = mock(IAlertReporter.class);

        InvolvedException result = IErrorStrategy.checkConnection(log, reporter).handleMdmException(e);

        assertEquals(ApplicationConstant.ERROR_CODE_0, result.errorCode);
        assertEquals(ApplicationConstant.ERROR_CANNOT_EXECUTE_REQUEST, result.errorKind);
        assertEquals(Status.INTERNAL_SERVER_ERROR, result.status);
    }

    @Test
    public void testCallsTheAlertReporter() {
        LoggerAdapter log = mock(LoggerAdapter.class);
        IAlertReporter reporter = mock(IAlertReporter.class);

        InvolvedException result = IErrorStrategy.checkConnection(log, reporter).handleMdmException(e);

        verify(reporter, times(1)).reportAlert(e);
    }
}
