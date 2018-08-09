package org.invertthepyramid.involved.misc;

import org.junit.Assert;
import org.junit.Test;


public class MDMServiceExceptionTest {


    @Test
    public void testConstructorAndGetters(){
        MDMServiceException e = new MDMServiceException(123, "someMessage");
        Assert.assertEquals(123, e.getErrorId());
        Assert.assertEquals("someMessage", e.getThrowableMessage());
    }

    @Test
    public void testIsConnectionResetIsFalseIfErrorIdIsNotZero(){
        Assert.assertFalse(new MDMServiceException(1, "someMessage").isConnectionReset());
        Assert.assertFalse(new MDMServiceException(2, "Connection reset by peer at remote address").isConnectionReset());
    }
    @Test
    public void testIsConnectionResetIsFalseIfErrorIdIsZeroButTextNotPresent(){
        Assert.assertFalse(new MDMServiceException(0, "Connection text not here").isConnectionReset());
    }
    @Test
    public void testIsConnectionResetIsTrueIfErrorIsZeroAndMessageHasExpectedResetText(){
        Assert.assertTrue(new MDMServiceException(0, "xxx Connection reset by peer at remote addressxxx").isConnectionReset());
    }

}
