package one.xingyi.core.endpoints;
import one.xingyi.core.http.ServiceResponse;

import java.util.Optional;
public interface HasBookmarkAndUrl {
    BookmarkAndUrlPattern bookmarkAndUrl();
    default EndPoint entityEndpoint() {return EndPoint.staticEndpoint(EndpointAcceptor0.exact("get", bookmarkAndUrl().bookmark), ServiceResponse.jsonString(200, bookmarkAndUrl().toJson()));}


}
