package org.invertthepyramid.involved;

import org.invertthepyramid.involved.misc.EventType;
import org.invertthepyramid.involved.misc.MDMServiceException;
import org.invertthepyramid.involved.misc.Severity;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class AlertReporterTest {

    @Test
    public void testAlertDetailsWhenConnectionReset() {
        MDMServiceException e = mock(MDMServiceException.class);
        when(e.isConnectionReset()).thenReturn(true);
        AlertDetails a = AlertDetails.create(e);
        assertEquals("IP001", a.level);
        assertEquals(Severity.CRITICAL, a.severity);
        assertEquals(EventType.TECHNICAL, a.eventType);
        assertEquals(e, a.mdmec);
    }

    @Test
    public void testAlertDetailsWhenConnectionNotReset() {
        MDMServiceException e = mock(MDMServiceException.class);
        when(e.isConnectionReset()).thenReturn(false);
        AlertDetails a = AlertDetails.create(e);
        assertEquals("IP002", a.level);
        assertEquals(Severity.FATAL, a.severity);
        assertEquals(EventType.TECHNICAL, a.eventType);
        assertEquals(e, a.mdmec);
    }

    @Test
    public void testAlertDetailsMessage() {
        MDMServiceException e = new MDMServiceException(123, "someMessage");
        //Haven't implemented this yet properly
        assertEquals("someMessage", new AlertDetails(e, EventType.TECHNICAL, Severity.CRITICAL, "someLevel").message("stage"));
    }

    @Test
    public void testCallsTryLogWithMessage() {
        MDMServiceException e = new MDMServiceException(123, "someMessage");
        String expectedMessage = AlertDetails.create(e).message("stage");
        LoggerAdapter log = mock(LoggerAdapter.class);
        IAlertReporter.reporter(log, "stage").reportAlert(e);

        ArgumentCaptor<Getter> captor = ArgumentCaptor.forClass(Getter.class);
        verify(log, times(1)).tryError(eq("MDM"), captor.capture());
        assertEquals(expectedMessage, captor.getValue().get());

    }
}
