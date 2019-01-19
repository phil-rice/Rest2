package one.xingyi.core.annotationProcessors;
import lombok.RequiredArgsConstructor;
import one.xingyi.core.annotations.Field;
import one.xingyi.core.codeDom.FieldDom;
import one.xingyi.core.names.EntityNames;
import one.xingyi.core.names.IServerNames;
import one.xingyi.core.typeDom.TypeDom;
import one.xingyi.core.utils.Optionals;
import one.xingyi.core.validation.Result;

import javax.lang.model.element.Element;
import java.util.Optional;
import java.util.function.Function;
public interface IElementToFieldDom extends Function<Element, Result<ElementFail, FieldDom>> {
    static IElementToFieldDom simple(ElementToBundle bundle, EntityNames entityNames) {return new SimpleElementToFieldDom(bundle.serverNames(),entityNames);}
}
@RequiredArgsConstructor
class SimpleElementToFieldDom implements IElementToFieldDom {
    final IServerNames serverNames;
    final EntityNames entityNames;
    @Override public Result<ElementFail, FieldDom> apply(Element element) {
        String fieldType = element.asType().toString();
        String fieldName = element.getSimpleName().toString();
        Optional<TypeDom> typeDom = TypeDom.create(fieldType);
        if (typeDom.isEmpty()) return Result.fail(new ElementFail("Could not handle the type " + fieldType + " for " + fieldName, element));
        Field annotation = element.getAnnotation(Field.class);
        String lensName = Optionals.chain(annotation, f -> f.lensName(), "", f -> serverNames.entityLensName(entityNames, fieldName, f));
        String lensPath = Optionals.chain(annotation, f -> f.lensPath(), "", f -> serverNames.entityLensPath(entityNames, fieldName, f));
        Boolean readOnly = Optional.ofNullable(annotation).map(Field::readOnly).orElse(false);
        Optional<String> javascript = Optional.ofNullable(annotation).map(Field::javascript);
        return Result.succeed(new FieldDom(typeDom.get(), fieldName, readOnly, lensName, lensPath, javascript));
    }
}

