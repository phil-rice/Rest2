package one.xingyi.core.annotationProcessors;
import lombok.RequiredArgsConstructor;
import one.xingyi.core.annotations.*;
import one.xingyi.core.codeDom.EntityDom;
import one.xingyi.core.endpoints.BookmarkAndUrlPattern;
import one.xingyi.core.names.EntityNames;
import one.xingyi.core.names.IServerNames;
import one.xingyi.core.utils.Lists;
import one.xingyi.core.validation.Result;

import javax.lang.model.element.TypeElement;
import java.util.List;
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
        String bookmark = annotation.bookmark();
        String url = annotation.rootUrl();
        Optional<BookmarkAndUrlPattern> bookmarkAndUrlPattern = serverNames.bookmarkAndUrl(entityNames, bookmark, url);
        List<PostDom> pathDoms = Lists.collect(element.getEnclosedElements(), e -> e.getAnnotation(Post.class) != null, e -> PostDom.create(e.getSimpleName().toString(), e.getAnnotation(Post.class), url));
        ActionsDom actionsDom = new ActionsDom(//TODO Move into own mini interface
                Optional.ofNullable(element.getAnnotation(Get.class)).map(get -> new GetDom(get.mustExist())),
                Optional.ofNullable(element.getAnnotation(Put.class)).map(put -> new PutDom()),
                Optional.ofNullable(element.getAnnotation(Delete.class)).map(delete -> new DeleteDom()),
                Optional.ofNullable(element.getAnnotation(Create.class)).map(crate -> new CreateDom()),
                Optional.ofNullable(element.getAnnotation(CreateWithoutId.class)).map(create -> new CreateWithoutIdDom(create.url())),
                pathDoms
        );
        return elementToFieldListDom.apply(element).map(fieldListDom -> new EntityDom(element.getAnnotation(Deprecated.class) != null, entityNames, bookmarkAndUrlPattern, fieldListDom, actionsDom));
    }
}
