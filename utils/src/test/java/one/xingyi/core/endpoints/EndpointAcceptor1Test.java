package one.xingyi.core.endpoints;
import one.xingyi.core.http.ServiceRequest;
import org.junit.Test;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.Assert.*;
public class EndpointAcceptor1Test {

    EndpointAcceptor1<String> startEnd = EndpointAcceptor1.bookmarkAcceptor("get", "/some/<id>/atend", (sr,s)->sr.url.getPath() + ".." + s);
    EndpointAcceptor1<String> start = EndpointAcceptor1.bookmarkAcceptor("get", "/some/<id>", (sr,s)->sr.url.getPath() + ".." + s);
    EndpointAcceptor1<String> end = EndpointAcceptor1.bookmarkAcceptor("get", "<id>/end", (sr,s)->sr.url.getPath() + ".." + s);

    ServiceRequest sr(String method, String url) { return new ServiceRequest(method, url, List.of(), "");}
    @Test
    public void testBookmarkedJustStartSpecified() {
        assertEquals(Optional.of("/some/123..123"), start.apply(sr("get", "/some/123")));

        assertEquals(Optional.empty(), start.apply(sr("get", "/")));
        assertEquals(Optional.empty(), start.apply(sr("get", "/some/123/asdk")));
        assertEquals(Optional.empty(), start.apply(sr("get", "/123/end")));

    }

}