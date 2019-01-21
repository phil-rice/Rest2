package one.xingyi.core.annotationProcessors;
import lombok.RequiredArgsConstructor;
import one.xingyi.core.annotations.Entity;
import one.xingyi.core.codeDom.EntityDom;
import one.xingyi.core.endpoints.BookmarkAndUrlPattern;
import one.xingyi.core.names.EntityNames;
import one.xingyi.core.names.IServerNames;
import one.xingyi.core.utils.Optionals;
import one.xingyi.core.utils.Strings;
import one.xingyi.core.validation.Result;

import javax.lang.model.element.TypeElement;
import java.util.Optional;
import java.util.function.Function;
public interface IElementToEntityDom extends Function<TypeElement, Result<ElementFail, EntityDom>> {
    static IElementToEntityDom simple(ElementToBundle bundle, EntityNames entityNames) {return new SimpleElementToEntityDom(bundle.serverNames(), entityNames, bundle.elementToFieldListDomForEntity(entityNames));}
}
@RequiredArgsConstructor
class SimpleElementToEntityDom implements IElementToEntityDom {
    final IServerNames serverNames;
    final EntityNames entityNames;
    final IElementToFieldListDom elementToFieldListDom;
    @Override public Result<ElementFail, EntityDom> apply(TypeElement element) {
        Entity annotation = element.getAnnotation(Entity.class);
        Optional<BookmarkAndUrlPattern> bookmarkAndUrlPattern = serverNames.bookmarkAndUrl(entityNames, annotation.bookmark(), annotation.getUrl());
        return elementToFieldListDom.apply(element).map(fieldListDom -> new EntityDom(entityNames, bookmarkAndUrlPattern, fieldListDom));
    }
}
