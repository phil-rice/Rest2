package one.xingyi.core.annotationProcessors;
import lombok.RequiredArgsConstructor;
import one.xingyi.core.annotations.Entity;
import one.xingyi.core.codeDom.EntityDom;
import one.xingyi.core.names.EntityNames;
import one.xingyi.core.names.IServerNames;
import one.xingyi.core.utils.Optionals;
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
        Optional<String> optBookmark = Optionals.chainOpt(annotation, Entity::bookmark, b -> serverNames.bookmark(entityNames, b));
        Optional<String> optGetUrl = Optionals.chainOpt(annotation, Entity::getUrl, b -> serverNames.getUrl(entityNames, b));
        return elementToFieldListDom.apply(element).map(fieldListDom -> new EntityDom(entityNames, optBookmark, optGetUrl, fieldListDom));
    }
}
