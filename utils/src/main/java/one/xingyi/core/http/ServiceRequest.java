package one.xingyi.core.http;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import one.xingyi.core.utils.Lists;

import java.net.URI;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ToString
@EqualsAndHashCode

public class ServiceRequest {
    final public String method;
    final public URI url;
    final public List<Header> headers;
    final public String body;
    public ServiceRequest(String method, String url, List<Header> headers, String body) {
        this.method = method;
        this.url = URI.create(url);
        this.headers = headers;
        this.body = body;
    }
    private List<String> urlSegments = null;

    public int segmentsCount() {return urlSegments().size();}
    public List<String> urlSegments() {
        if (urlSegments == null)
            urlSegments = Lists.map(Arrays.asList(url.getPath().split("/")), s -> URLDecoder.decode(s, "UTF-8"));
        return urlSegments;
    }
    public String lastSegment() { return urlSegments().get(urlSegments().size() - 1); }


    public Optional<String> header(String header) {
        for (Header h : headers)
            if (h.name.equalsIgnoreCase(header))
                return Optional.of(h.value);
        return Optional.empty();
    }
}
