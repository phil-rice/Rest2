package one.xingyi.core.endpoints;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.utils.Strings;
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class BookmarkAndUrlPattern {
    public final String bookmark;
    public final String urlPattern;
    public static BookmarkAndUrlPattern invalid = new BookmarkAndUrlPattern("invalid", "invalid");
    public String toJson() {return Strings.changeQuotes("{'urlPattern':'" + urlPattern + "'}");}

}