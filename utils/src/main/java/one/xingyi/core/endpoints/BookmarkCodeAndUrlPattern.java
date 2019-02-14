package one.xingyi.core.endpoints;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.utils.Strings;
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class BookmarkCodeAndUrlPattern {
    public final String bookmark;
    public final String urlPattern;
    public final String code;

    public BookmarkCodeAndUrlPattern withUrl(String url){return new BookmarkCodeAndUrlPattern(bookmark, url, code);}
    public BookmarkCodeAndUrlPattern withMoreUrl(String extra){return new BookmarkCodeAndUrlPattern(bookmark, urlPattern+extra, code);}

    public static BookmarkCodeAndUrlPattern invalid = new BookmarkCodeAndUrlPattern("invalid", "invalid", "invalid");
}