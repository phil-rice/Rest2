package one.xingyi.core.annotationProcessors;
import one.xingyi.core.codeDom.EntityDom;
import one.xingyi.core.codeDom.ViewDom;

import javax.lang.model.element.Element;
import java.util.List;
import java.util.function.Function;
public interface IElementToViewDom {
    static IElementToViewDom simple = new SimpleElementToViewDom();

    ViewDom apply(List<EntityDom> entityDoms, Element viewElement);
}
class SimpleElementToViewDom implements IElementToViewDom {
    @Override public ViewDom apply(List<EntityDom> entityDoms, Element viewElement) {
        return null;
    }
}
