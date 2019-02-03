package one.xingyi.core.filemaker;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.annotationProcessors.ActionsDom;
import one.xingyi.core.codeDom.ViewDomAndItsResourceDom;
import one.xingyi.core.endpoints.BookmarkCodeAndUrlPattern;

import java.util.Optional;
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
class BookmarkUrlAndActionsDom {
    final BookmarkCodeAndUrlPattern bookmarkCodeAndUrlPattern;
    final ActionsDom actionsDom;

    static Optional<BookmarkUrlAndActionsDom> create(ViewDomAndItsResourceDom dom) {return dom.entityDom.flatMap(ed -> ed.bookmark.map(b -> new BookmarkUrlAndActionsDom(b, ed.actionsDom)));}

}
