package one.xingyi.core.endpoints;
import one.xingyi.core.utils.FunctionFixture;
import org.junit.Test;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static one.xingyi.core.http.ServiceRequest.sr;
import static org.junit.Assert.*;
public class ResourceWithFromEndpointAcceptorTest implements FunctionFixture {

    ResourceWithFromEndpointAcceptor<String> acceptor = new ResourceWithFromEndpointAcceptor<>("someMethod", "/somePath/{id}", (sr, s) -> sr.url + ":" + s);

    @Test
    public void testAccessors() {
        assertEquals("someMethod", acceptor.method());
        assertEquals("/somePath/{id}", acceptor.templatedPath());
    }

    @Test public void testApply() {
        assertEquals(Optional.empty(), acceptor.apply(sr("wrongMethod", "/somePath/1")));
        assertEquals(Optional.empty(), acceptor.apply(sr("someMethod", "/someWrongPath/1")));
        assertEquals(Optional.empty(), acceptor.apply(sr("someMethod", "/somePath/1/WithStuffOnEnd")));
        assertEquals(Optional.of("/somePath/someId:someId"), acceptor.apply(sr("someMethod", "/somePath/someId")));
    }

    @Test public void testAndIfMatches() throws ExecutionException, InterruptedException {
        assertEquals(Optional.of("123"), acceptor.andIfMatches(kleisli("/somePath/someId:someId", "123")).apply(sr("someMethod", "/somePath/someId")).get());
    }

}