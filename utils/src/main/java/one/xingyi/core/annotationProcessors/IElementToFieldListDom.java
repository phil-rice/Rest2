package one.xingyi.core.annotationProcessors;
import lombok.RequiredArgsConstructor;
import one.xingyi.core.codeDom.FieldDom;
import one.xingyi.core.codeDom.FieldListDom;
import one.xingyi.core.names.EntityNames;
import one.xingyi.core.names.IServerNames;
import one.xingyi.core.utils.Lists;
import one.xingyi.core.validation.Result;

import javax.lang.model.element.TypeElement;
import java.util.List;
import java.util.function.Function;
public interface IElementToFieldListDom extends Function<TypeElement, Result<ElementFail, FieldListDom>> {
    static IElementToFieldListDom simple(ElementToBundle bundle, EntityNames entityNames) {return new SimpleElementToFieldListDom(bundle.elementToFieldDom(entityNames),bundle.serverNames());}

}
@RequiredArgsConstructor
class SimpleElementToFieldListDom implements IElementToFieldListDom {
    final IElementToFieldDom elementToFieldDom;
    final IServerNames serverNames;

    @Override public Result<ElementFail, FieldListDom> apply(TypeElement element) {
        List<Result<ElementFail, FieldDom>> fieldResult = Lists.map(element.getEnclosedElements(), e -> elementToFieldDom.apply(e));
        Result<ElementFail, FieldListDom> result = Result.merge(fieldResult).map(FieldListDom::new);
        return result;
    }
}
