package one.xingyi.core.annotationProcessors;
import lombok.RequiredArgsConstructor;
import one.xingyi.core.annotations.*;
import one.xingyi.core.codeDom.ResourceDom;
import one.xingyi.core.endpoints.BookmarkCodeAndUrlPattern;
import one.xingyi.core.names.EntityNames;
import one.xingyi.core.names.IServerNames;
import one.xingyi.core.utils.Lists;
import one.xingyi.core.validation.Result;

import javax.lang.model.element.TypeElement;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
public interface IElementToEntityDom extends BiFunction<TypeElement, IViewDefnNameToViewName, Result<ElementFail, ResourceDom>> {
    static IElementToEntityDom simple(ElementToBundle bundle, EntityNames entityNames) {return new SimpleElementToEntityDom(bundle.serverNames(), entityNames, bundle.elementToFieldListDomForEntity(entityNames));}
}
@RequiredArgsConstructor
class SimpleElementToEntityDom implements IElementToEntityDom {
    final IServerNames serverNames;
    final EntityNames entityNames;
    final IElementToFieldListDom elementToFieldListDom;
    @Override public Result<ElementFail, ResourceDom> apply(TypeElement element, IViewDefnNameToViewName viewNamesMap) {
        Resource annotation = element.getAnnotation(Resource.class);
        String bookmark = annotation.bookmark();
        String url = annotation.urlWithId();
        Optional<BookmarkCodeAndUrlPattern> bookmarkAndUrlPattern = serverNames.bookmarkAndUrl(entityNames, bookmark, url, annotation.codeUrl());
        List<PostDom> pathDoms = Lists.collect(element.getEnclosedElements(), e -> e.getAnnotation(Post.class) != null, e -> PostDom.create(e.getSimpleName().toString(), e.getAnnotation(Post.class), url));
        ActionsDom actionsDom = new ActionsDom(//TODO Move into own mini interface
                Optional.ofNullable(element.getAnnotation(Get.class)).map(get -> new GetDom()),
                Optional.ofNullable(element.getAnnotation(OptionalGet.class)).map(get -> new OptionalGetDom()),
                Optional.ofNullable(element.getAnnotation(Put.class)).map(put -> new PutDom()),
                Optional.ofNullable(element.getAnnotation(Delete.class)).map(delete -> new DeleteDom()),
                Optional.ofNullable(element.getAnnotation(Create.class)).map(crate -> new CreateDom()),
                Optional.ofNullable(element.getAnnotation(CreateWithoutId.class)).map(create -> new CreateWithoutIdDom(create.url())),
                Optional.ofNullable(element.getAnnotation(Prototype.class)).map(prototype -> new PrototypeDom(prototype.value())),
                Optional.ofNullable(element.getAnnotation(PrototypeNoId.class)).map(dom -> new PrototypeNoIdDom(dom.prototypeId(), dom.url())),
                pathDoms
        );
        return elementToFieldListDom.apply(element, viewNamesMap, Optional.empty()).map(fieldListDom -> new ResourceDom(element.getAnnotation(Deprecated.class) != null, entityNames, bookmarkAndUrlPattern, fieldListDom, actionsDom));
    }
}
