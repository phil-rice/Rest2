package one.xingyi.core.endpoints;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class BookmarkAndUrlPattern {
    public final String bookmark;
    public final String urlPattern;
    public static BookmarkAndUrlPattern invalid = new BookmarkAndUrlPattern("invalid", "invalid");
}