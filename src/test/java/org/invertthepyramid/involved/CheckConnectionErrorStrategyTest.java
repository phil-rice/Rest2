package org.invertthepyramid.involved;

import org.invertthepyramid.involved.misc.ApplicationConstant;
import org.invertthepyramid.involved.misc.MDMServiceException;
import org.invertthepyramid.involved.misc.Status;
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
