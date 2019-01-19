package one.xingyi.core.annotationProcessors;
import lombok.RequiredArgsConstructor;
import one.xingyi.core.codeDom.ViewDom;
import one.xingyi.core.names.ViewNames;
import one.xingyi.core.validation.Result;

import javax.lang.model.element.TypeElement;
public interface IElementToViewDom {
    static IElementToViewDom forView(ElementToBundle bundle, ViewNames viewNames) { return new SimpleElementToViewDom(viewNames, bundle.elementToFieldListDomForView(viewNames));}

    Result<ElementFail, ViewDom> apply(TypeElement viewElement);
}
@RequiredArgsConstructor
class SimpleElementToViewDom implements IElementToViewDom {
    final ViewNames viewNames;
    final IElementToFieldListDom elementToFieldListDom;


    @Override public Result<ElementFail, ViewDom> apply(TypeElement viewElement) {
        return elementToFieldListDom.apply(viewElement).map(fieldListDom -> new ViewDom(viewNames, "not worked out yet", fieldListDom));
    }
}
