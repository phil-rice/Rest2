package one.xingyi.core.annotationProcessors;
import lombok.RequiredArgsConstructor;
import one.xingyi.core.annotations.Field;
import one.xingyi.core.codeDom.FieldDom;
import one.xingyi.core.names.EntityNames;
import one.xingyi.core.names.IServerNames;
import one.xingyi.core.names.ViewNames;
import one.xingyi.core.typeDom.TypeDom;
import one.xingyi.core.utils.Optionals;
import one.xingyi.core.validation.Result;

import javax.lang.model.element.Element;
import java.util.Optional;
import java.util.function.Function;
public interface IElementToFieldDom extends Function<Element, Result<ElementFail, FieldDom>> {
    static IElementToFieldDom forEntity(ElementToBundle bundle, EntityNames entityNames) {return new SimpleElementToFieldDomForEntity(bundle.serverNames(), entityNames);}
    static IElementToFieldDom forView(ElementToBundle bundle, ViewNames viewNames) {return new SimpleElementToFieldDomForViews(bundle.serverNames(), viewNames);}
}

@RequiredArgsConstructor
abstract class AbstractElementToFieldDom implements IElementToFieldDom {
    final IServerNames serverNames;
    abstract String findLensName(String fieldName, String annotationField);
    abstract String findLensPath(String fieldName, String annotationField);
    @Override public Result<ElementFail, FieldDom> apply(Element element) {
        String fieldType = element.asType().toString();
        String fieldName = element.getSimpleName().toString();
        Optional<TypeDom> typeDom = TypeDom.create(fieldType);
        if (typeDom.isEmpty()) return Result.fail(new ElementFail("Could not handle the type " + fieldType + " for " + fieldName, element));
        Field annotation = element.getAnnotation(Field.class);
        String lensName = Optionals.chain(annotation, f -> f.lensName(), "", f -> findLensName(fieldName, f));
        String lensPath = Optionals.chain(annotation, f -> f.lensPath(), "", f -> findLensPath(fieldName, f));
        Boolean readOnly = Optional.ofNullable(annotation).map(Field::readOnly).orElse(false);
        Optional<String> javascript = Optional.ofNullable(annotation).map(Field::javascript);
        return Result.succeed(new FieldDom(typeDom.get(), fieldName, readOnly, lensName, lensPath, javascript));
    }
}
class SimpleElementToFieldDomForEntity extends AbstractElementToFieldDom {
    final EntityNames entityNames;
    public SimpleElementToFieldDomForEntity(IServerNames serverNames, EntityNames entityNames) {
        super(serverNames);
        this.entityNames = entityNames;
    }
    @Override String findLensName(String fieldName, String annotationField) {
        return serverNames.entityLensPath(entityNames, fieldName, annotationField);
    }
    @Override String findLensPath(String fieldName, String annotationField) {
        return serverNames.entityLensName(entityNames, fieldName, annotationField);
    }
}

class SimpleElementToFieldDomForViews extends AbstractElementToFieldDom {
    final ViewNames viewNames;

    public SimpleElementToFieldDomForViews(IServerNames serverNames, ViewNames viewNames) {
        super(serverNames);
        this.viewNames = viewNames;
    }
    @Override String findLensName(String fieldName, String annotationField) {
        return "lens for views not done yet";
    }
    @Override String findLensPath(String fieldName, String annotationField) {
        return "lens path for views not done yet";
    }
}

