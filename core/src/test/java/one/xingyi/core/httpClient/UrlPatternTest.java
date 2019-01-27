package one.xingyi.core.httpClient;
import one.xingyi.core.httpClient.client.companion.UrlPatternCompanion;
import one.xingyi.core.httpClient.client.view.UrlPattern;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
public class UrlPatternTest {

    Function<UrlPattern, String> getFn = UrlPattern::urlPattern;
    String url = "http://localhost:9000/bookmarkAndUrl";
    @Test
    public void testDelegatesGetPrimitiveToHttpService() throws ExecutionException, InterruptedException {
        HttpService service = mock(HttpService.class);
        when(service.primitive(UrlPatternCompanion.companion,"get", url, getFn)).thenReturn(CompletableFuture.completedFuture("someUrlPattern"));
        String actual = UrlPatternCompanion.companion.primitive(service, "get", url, getFn).get();
        assertEquals("someUrlPattern", actual);
    }

    @Test
    public void testDelegatesCallToHttpService() throws ExecutionException, InterruptedException {
        HttpService service = mock(HttpService.class);
        when(service.get(UrlPatternCompanion.companion, "someId", getFn)).thenReturn(CompletableFuture.completedFuture("someUrlPattern"));
        String actual = UrlPattern.get(service, "someId", getFn).get();
        assertEquals("someUrlPattern", actual);
    }

}
