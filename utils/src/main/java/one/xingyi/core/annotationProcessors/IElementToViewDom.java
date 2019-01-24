package one.xingyi.core.annotationProcessors;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.annotations.*;
import one.xingyi.core.codeDom.ViewDom;
import one.xingyi.core.names.ViewNames;
import one.xingyi.core.utils.Strings;
import one.xingyi.core.validation.Result;

import javax.lang.model.element.TypeElement;
import java.util.Optional;
public interface IElementToViewDom {
    static IElementToViewDom forView(ElementToBundle bundle, ViewNames viewNames) { return new SimpleElementToViewDom(viewNames, bundle.elementToFieldListDomForView(viewNames));}

    Result<ElementFail, ViewDom> apply(TypeElement viewElement);
}
@RequiredArgsConstructor
class SimpleElementToViewDom implements IElementToViewDom {
    final ViewNames viewNames;
    final IElementToFieldListDom elementToFieldListDom;


    @Override public Result<ElementFail, ViewDom> apply(TypeElement viewElement) {
        ActionsDom actionsDom = new ActionsDom(
                Optional.ofNullable(viewElement.getAnnotation(Get.class)).map(get -> new GetDom(get.value())),
                Optional.ofNullable(viewElement.getAnnotation(Put.class)).map(put -> new PutDom(put.value())),
                Optional.ofNullable(viewElement.getAnnotation(Delete.class)).map(put -> new DeleteDom(put.value())),
                Optional.ofNullable(viewElement.getAnnotation(Create.class)).map(put -> new CreateDom(put.value())),
                Optional.ofNullable(viewElement.getAnnotation(CreateWithoutId.class)).map(put -> new CreateWithoutIdDom(put.value()))
        );
        return elementToFieldListDom.apply(viewElement).map(fieldListDom -> new ViewDom(viewNames, actionsDom, fieldListDom));
    }
}

