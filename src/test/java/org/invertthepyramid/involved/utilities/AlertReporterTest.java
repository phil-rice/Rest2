package org.invertthepyramid.involved.utilities;

import org.invertthepyramid.involved.FunctionsFixture;
import org.invertthepyramid.involved.mdm.EventType;
import org.invertthepyramid.involved.mdm.MDMServiceException;
import org.invertthepyramid.involved.domain.Severity;
import org.invertthepyramid.involved.utilities.*;
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
    public void testCallsTryLogAndCreatesAlertDetailsAndLogsMessage() {
        MDMServiceException e = mock(MDMServiceException.class);
        AlertDetails alertDetails = mock(AlertDetails.class);
        when(alertDetails.message("stage")).thenReturn("someMessage");
        LoggerAdapter log = mock(LoggerAdapter.class);

        AlertReporter logger = (AlertReporter) IAlertReporter.reporter(log, "stage");
        logger.detailsMaker = FunctionsFixture.constant(e, alertDetails);

        logger.reportAlert(e);

        ArgumentCaptor<Getter> captor = ArgumentCaptor.forClass(Getter.class);
        verify(log, times(1)).tryError(eq("MDM"), captor.capture());
        assertEquals("someMessage", captor.getValue().get());

    }
}
