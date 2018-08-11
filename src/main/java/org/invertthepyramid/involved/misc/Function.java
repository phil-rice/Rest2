package org.invertthepyramid.involved.misc;

import com.twitter.util.Await;
import com.twitter.util.Future;

public class Function {
    public static <T> T destroyMyPerformance(Future<T> responseFuture) {
        try {
            return Await.result(responseFuture);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
