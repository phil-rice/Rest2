package one.xingyi.core.annotationProcessors;
import lombok.RequiredArgsConstructor;
import one.xingyi.core.annotations.Post;
import one.xingyi.core.codeDom.FieldDom;
import one.xingyi.core.codeDom.FieldListDom;
import one.xingyi.core.codeDom.ResourceDom;
import one.xingyi.core.codeDom.ViewDom;
import one.xingyi.core.names.EntityNames;
import one.xingyi.core.names.IServerNames;
import one.xingyi.core.names.ViewNames;
import one.xingyi.core.utils.Function3;
import one.xingyi.core.utils.Lists;
import one.xingyi.core.validation.Result;

import javax.lang.model.element.TypeElement;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
public interface IElementToFieldListDom extends Function3<TypeElement, IViewDefnNameToViewName, Optional<ResourceDom>, Result<ElementFail, FieldListDom>> {
    static IElementToFieldListDom forEntities(ElementToBundle bundle, EntityNames entityNames) {return new SimpleElementToFieldListDom(bundle.elementToFieldDomForEntity(entityNames), bundle.serverNames());}
    static IElementToFieldListDom forViews(ElementToBundle bundle, ViewNames viewNames) {return new SimpleElementToFieldListDom(bundle.elementToFieldDomForView(viewNames), bundle.serverNames());}

}
@RequiredArgsConstructor
class SimpleElementToFieldListDom implements IElementToFieldListDom {
    final IElementToFieldDom elementToFieldDom;
    final IServerNames serverNames;

    @Override public Result<ElementFail, FieldListDom> apply(TypeElement element, IViewDefnNameToViewName viewNamesMap, Optional<ResourceDom> optResourceDom) {
        List<Result<ElementFail, FieldDom>> fieldResult = Lists.collect(element.getEnclosedElements(), e -> e.getAnnotation(Post.class) == null, e -> elementToFieldDom.apply(e, viewNamesMap, optResourceDom));
        Result<ElementFail, FieldListDom> result = Result.merge(fieldResult).map(FieldListDom::new);
        return result;
    }

}
