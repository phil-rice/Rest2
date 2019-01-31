package one.xingyi.core.filemaker;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.annotationProcessors.ActionsDom;
import one.xingyi.core.codeDom.ViewDomAndItsResourceDom;
import one.xingyi.core.endpoints.BookmarkAndUrlPattern;

import java.util.Optional;
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
class BookmarkUrlAndActionsDom {
    final BookmarkAndUrlPattern bookmarkAndUrlPattern;
    final ActionsDom actionsDom;

    static Optional<BookmarkUrlAndActionsDom> create(ViewDomAndItsResourceDom dom) {return dom.entityDom.flatMap(ed -> ed.bookmark.map(b -> new BookmarkUrlAndActionsDom(b, ed.actionsDom)));}

}
