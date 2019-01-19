package one.xingyi.core.annotationProcessors;
import one.xingyi.core.codeDom.EntityDom;

import javax.lang.model.element.Element;
import java.util.function.Function;
public interface IElementToEntityDom extends Function<Element, EntityDom> {
    static IElementToEntityDom simple = new SimpleElementToEntityDom();
}
class SimpleElementToEntityDom implements IElementToEntityDom {
    @Override public EntityDom apply(Element element) {
        return new EntityDom();
    }
}
