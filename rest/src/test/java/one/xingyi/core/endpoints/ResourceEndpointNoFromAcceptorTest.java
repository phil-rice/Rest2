package one.xingyi.core.endpoints;
import one.xingyi.core.utils.FunctionFixture;
import org.junit.Test;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static one.xingyi.core.http.ServiceRequest.sr;
import static org.junit.Assert.*;
public class ResourceEndpointNoFromAcceptorTest implements FunctionFixture {

    ResourceEndpointNoFromAcceptor acceptor = new ResourceEndpointNoFromAcceptor("someMethod", "/somePath","test");

    @Test public void testAccessors() {
        assertEquals("someMethod", acceptor.method());
        assertEquals("/somePath", acceptor.templatedPath());
    }

    @Test public void testApply() {
        assertEquals(Optional.empty(), acceptor.apply(sr("wrongMethod", "/somePath")));
        assertEquals(Optional.empty(), acceptor.apply(sr("someMethod", "/someWrongPath")));
        assertEquals(Optional.empty(), acceptor.apply(sr("someMethod", "/somePath/WithStuffOnEnd")));
        assertEquals(SuccessfulMatch.optMatch, acceptor.apply(sr("someMethod", "/somePath")));
    }

    @Test public void testAndIfMatches() throws ExecutionException, InterruptedException {
        assertEquals(Optional.of("123"), acceptor.andIfMatches(kleisli(SuccessfulMatch.match, "123")).apply(sr("someMethod", "/somePath")).get());
    }
}