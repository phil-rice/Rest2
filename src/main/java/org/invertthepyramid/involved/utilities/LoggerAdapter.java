package org.invertthepyramid.involved.utilities;

import org.invertthepyramid.involved.utilities.Getter;

public class LoggerAdapter {

    public void tryError(String prefix, Getter<String> message) {

        try {
            error(message.get());
        } catch (Exception ex) {
            error(prefix + " problem and problem logging to Alert", ex);
        }
    }

    public void error(String s, Object... objects) {

    }

    public void warn(String s, String partyId) {
    }
}
