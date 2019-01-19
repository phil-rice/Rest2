package one.xingyi.core.annotationProcessors;
import lombok.RequiredArgsConstructor;
import one.xingyi.core.annotations.Entity;
import one.xingyi.core.names.EntityNames;
import one.xingyi.core.names.IServerNames;
import one.xingyi.core.validation.Result;

import javax.lang.model.element.Element;
import java.util.function.Function;
public interface IElementToEntityNames extends Function<Element, Result<ElementFail, EntityNames>> {
    static IElementToEntityNames simple(IServerNames serverNames) {return new SimpleElementToEntityName(serverNames);}
}
@RequiredArgsConstructor
class SimpleElementToEntityName implements IElementToEntityNames {
    final IServerNames serverNames;
    @Override public Result<ElementFail, EntityNames> apply(Element element) {
        String className = element.asType().toString();
        String annotationEntityname = element.getAnnotation(Entity.class).entityName();
        return serverNames.entityName(className, annotationEntityname).failMap(s -> new ElementFail(s, element));
    }
}
