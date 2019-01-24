package one.xingyi.server;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import one.xingyi.core.endpoints.HasBookmarkAndUrl;
import one.xingyi.core.httpClient.EntityDetailsRequest;
import one.xingyi.core.httpClient.domain.EntityDetails;
import one.xingyi.core.utils.Lists;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public interface EntityRegister extends Function<EntityDetailsRequest, CompletableFuture<Optional<EntityDetails>>> {
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
    public CompletableFuture<Optional<EntityDetails>> apply(EntityDetailsRequest entityDetailsRequest) {
        Optional<EntityDetails> entity = Lists.find(companions, c -> c.bookmarkAndUrl().bookmark.equals(entityDetailsRequest.entityName)).
                map(b ->
                        b.bookmarkAndUrl().urlPattern).
                map(EntityDetails::new);
        return CompletableFuture.completedFuture(entity);
    }
}