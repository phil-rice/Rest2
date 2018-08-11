package org.invertthepyramid.involved;

import com.twitter.util.Await;
import org.invertthepyramid.involved.misc.MDMServiceException;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.twitter.util.Future;

public class WrapTest {

    @Test
    public void testWrapReturnsTheValueIfNoException() {
        IErrorStrategy strategy = mock(IErrorStrategy.class);
        int result = Wrap.wrap(strategy, FunctionsFixture.constantGetter(Future.value(123))).intValue();
        assertEquals(123, result);
        verify(strategy, times(0)).handleMdmException(any(MDMServiceException.class));
    }

    @Test
    public void testWrapThrowsTheResultOfTheStrategyIfMdmException() {
        MDMServiceException mdmServiceException = new MDMServiceException(33, "some message:");
        InvolvedException involvedException = new InvolvedException(123, "someError", 500);
        IErrorStrategy strategy = mock(IErrorStrategy.class);
        when(strategy.handleMdmException(mdmServiceException)).thenReturn(involvedException);
        try {
            Wrap.wrap(strategy, FunctionsFixture.constantGetter(Future.exception(mdmServiceException)));
            fail();
        } catch (InvolvedException e) {
            assertEquals(involvedException, e);
        }
    }

    @Test
    public void testWrapThrowsTheExceptionIfNonMdmException() {
        RuntimeException e = new RuntimeException();
        IErrorStrategy strategy = mock(IErrorStrategy.class);
        try {
            Wrap.wrap(strategy, FunctionsFixture.constantGetter(Future.exception(e)));
            fail();
        } catch (RuntimeException actual) {
            assertEquals(e, actual);
        }
        verify(strategy, times(0)).handleMdmException(any(MDMServiceException.class));
    }

    @Test
    public void testMapReturnsGetterHoldingAFutureOftheOriginalFutureModifiedByTheFn() throws Exception {
        Future<Integer> value = Future.value(5);
        Getter<Future<String>> getter = Wrap.map(value, (i) -> i.toString());
        Assert.assertEquals("5", Await.result(getter.get()));
    }


}
