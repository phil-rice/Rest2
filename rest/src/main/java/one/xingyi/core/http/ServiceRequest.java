package one.xingyi.core.http;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import one.xingyi.core.utils.Lists;
import one.xingyi.core.utils.Strings;
import one.xingyi.core.utils.WrappedException;

import java.net.URI;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

@ToString
@EqualsAndHashCode
public class ServiceRequest {
    public static Function<ServiceRequest, String> ripId(String bookmark, Function<ServiceRequest, RuntimeException> ifNotPresent) {
        Function<String, Optional<String>> ripper = Strings.ripIdFromPath(bookmark);
        return from -> ripper.apply(from.uri.getPath()).orElseThrow(() -> ifNotPresent.apply(from));
    }
    public static ServiceRequest sr(String method, String path) {return new ServiceRequest(method, path, List.of(), "");}
    public static ServiceRequest sr(String method, String path, String acceptHeader) {return new ServiceRequest(method, path, List.of(new Header("accept", acceptHeader)), "");}
    final public String method;
    final public String originalUrl;
    final public URI uri;
    final public String path;
    final public List<Header> headers;
    final public String body;
    public ServiceRequest(String method, String url, List<Header> headers, String body) {
        this.method = method;
        this.originalUrl = url;
        if (url.equalsIgnoreCase("invalid")) throw new RuntimeException("found invalid");
        this.uri = URI.create(url);
        this.path = this.uri.getPath();//this validates the url is correct early
        this.headers = headers;
        this.body = body;
    }
    private List<String> urlSegments = null;

    public int segmentsCount() {return urlSegments().size();}
    public List<String> urlSegments() {
        if (urlSegments == null)
            urlSegments = Lists.map(Arrays.asList(uri.getPath().split("/")), s -> URLDecoder.decode(s, "UTF-8"));
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
