package org.invertthepyramid.involved.misc;

import org.invertthepyramid.involved.LoggerAdapter;
import org.junit.Assert;
import org.junit.Test;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class LoggerAdapterTest {

    class RememberLogger extends LoggerAdapter {
        List<String> memory = new ArrayList<>();

        @Override
        public void error(String s, Object... objects) {
            memory.add(MessageFormat.format(s, objects));
        }
    }

    @Test
    public void testTryLogReportSendsTheMessageIfNoError() {
        RememberLogger log = new RememberLogger();
        log.tryError("irrelevant", () -> "someMessage");
        Assert.assertEquals(1, log.memory.size());
        Assert.assertEquals("someMessage", log.memory.get(0));
    }

    @Test
    public void testTryLogReportSendsTheExceptionIfThathappens() {
        RememberLogger log = new RememberLogger();
        RuntimeException e = new RuntimeException("someException");
        log.tryError("this prefix", () -> {throw e;});
        Assert.assertEquals(1, log.memory.size());
        Assert.assertEquals("this prefix problem and problem logging to Alert", log.memory.get(0));
    }
}
