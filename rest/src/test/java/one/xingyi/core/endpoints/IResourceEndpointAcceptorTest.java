package one.xingyi.core.endpoints;
import one.xingyi.core.http.ServiceRequest;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;
public class IResourceEndpointAcceptorTest {

    @Test public void testWithPost() {
        assertEquals(Optional.of(SuccessfulMatch.match), IResourceEndpointAcceptor.<String>apply("post", "/lights").apply(ServiceRequest.sr("post", "/lights")));
        assertEquals(Optional.empty(), IResourceEndpointAcceptor.<String>apply("post", "/lights").apply(ServiceRequest.sr("get", "/lights")));
        assertEquals(Optional.empty(), IResourceEndpointAcceptor.<String>apply("post", "/lights").apply(ServiceRequest.sr("post", "/lights1")));
        assertEquals(Optional.empty(), IResourceEndpointAcceptor.<String>apply("post", "/lights").apply(ServiceRequest.sr("post", "/lights/other")));
    }
    @Test public void testWithGet() {
        assertEquals(Optional.of("123_match"), IResourceEndpointAcceptor.<String>apply("get", "/lights/{id}", (sr, s) -> s + "_match").apply(ServiceRequest.sr("get", "/lights/123")));
        assertEquals(Optional.empty(), IResourceEndpointAcceptor.<String>apply("get", "/lights/{id}", (sr, s) -> s + "_match").apply(ServiceRequest.sr("get", "/lights")));
        assertEquals(Optional.empty(), IResourceEndpointAcceptor.<String>apply("get", "/lights/{id}", (sr, s) -> s + "_match").apply(ServiceRequest.sr("post", "/lights/123")));
    }
    @Test public void testWithPostWithHostPrefix() {
        assertEquals(Optional.of(SuccessfulMatch.match), IResourceEndpointAcceptor.<String>apply("post", "{host}/lights").apply(ServiceRequest.sr("post", "/lights")));
        assertEquals(Optional.empty(), IResourceEndpointAcceptor.<String>apply("post", "{host}/lights").apply(ServiceRequest.sr("get", "/lights")));
        assertEquals(Optional.empty(), IResourceEndpointAcceptor.<String>apply("post", "{host}/lights").apply(ServiceRequest.sr("post", "/lights1")));
        assertEquals(Optional.empty(), IResourceEndpointAcceptor.<String>apply("post", "{host}/lights").apply(ServiceRequest.sr("post", "/lights/other")));
    }
    @Test public void testWithGetWithHostPrefix() {
        assertEquals(Optional.of("123_match"), IResourceEndpointAcceptor.<String>apply("get", "{host}/lights/{id}", (sr, s) -> s + "_match").apply(ServiceRequest.sr("get", "/lights/123")));
        assertEquals(Optional.empty(), IResourceEndpointAcceptor.<String>apply("get", "{host}/lights/{id}", (sr, s) -> s + "_match").apply(ServiceRequest.sr("get", "/lights")));
        assertEquals(Optional.empty(), IResourceEndpointAcceptor.<String>apply("get", "{host}/lights/{id}", (sr, s) -> s + "_match").apply(ServiceRequest.sr("post", "/lights/123")));
    }
}