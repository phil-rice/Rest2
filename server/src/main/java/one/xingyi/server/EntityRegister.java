package one.xingyi.server;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import one.xingyi.core.endpoints.HasBookmarkAndUrl;
import one.xingyi.core.httpClient.ResourceDetailsRequest;
import one.xingyi.core.httpClient.server.domain.ResourceDetails;
import one.xingyi.core.utils.Lists;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public interface EntityRegister extends Function<ResourceDetailsRequest, Optional<ResourceDetails>> {
    List<String> registered();
    static EntityRegister apply(List<HasBookmarkAndUrl> bookmarkAndUrls) { return new SimpleEntityRegister(bookmarkAndUrls); }
    static EntityRegister apply(HasBookmarkAndUrl... bookmarkAndUrls) { return new SimpleEntityRegister(Arrays.asList(bookmarkAndUrls)); }
}


@EqualsAndHashCode
@ToString
class SimpleEntityRegister implements EntityRegister {
    private final List<String> legalValues;
    public SimpleEntityRegister(List<HasBookmarkAndUrl> companions) {
        this.companions = companions;
        this.legalValues = Lists.map(companions, c -> c.bookmarkAndUrl().bookmark);
    }
    final List<HasBookmarkAndUrl> companions;

    @Override
    public Optional<ResourceDetails> apply(ResourceDetailsRequest resourceDetailsRequest) {
        return Lists.find(companions, c -> c.bookmarkAndUrl().bookmark.equals(resourceDetailsRequest.entityName)).
                map(b ->
                        b.bookmarkAndUrl().urlPattern).
                map(ResourceDetails::new);
    }
    @Override public List<String> registered() { return legalValues; }
}
