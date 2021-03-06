package one.xingyi.core.annotationProcessors;
import lombok.RequiredArgsConstructor;
import one.xingyi.core.names.EntityNames;
import one.xingyi.core.names.IServerNames;

import javax.lang.model.element.Element;
import java.util.function.Function;
public interface IElementToEntityNames extends Function<Element,  EntityNames> {
    static IElementToEntityNames simple(IServerNames serverNames) {return new SimpleElementToEntityName(serverNames);}
}
@RequiredArgsConstructor
class SimpleElementToEntityName implements IElementToEntityNames {
    final IServerNames serverNames;
    @Override public EntityNames apply(Element element) {
        String className = element.asType().toString();
        return serverNames.entityName(className);
    }
}
