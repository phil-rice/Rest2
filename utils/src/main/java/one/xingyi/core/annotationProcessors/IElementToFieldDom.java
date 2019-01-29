package one.xingyi.core.annotationProcessors;
import lombok.RequiredArgsConstructor;
import one.xingyi.core.annotations.Field;
import one.xingyi.core.codeDom.FieldDom;
import one.xingyi.core.names.EntityNames;
import one.xingyi.core.names.IServerNames;
import one.xingyi.core.names.ViewNames;
import one.xingyi.core.typeDom.TypeDom;
import one.xingyi.core.utils.LoggerAdapter;
import one.xingyi.core.utils.Optionals;
import one.xingyi.core.utils.Strings;
import one.xingyi.core.validation.Result;

import javax.lang.model.element.Element;
import java.util.Optional;
import java.util.function.Function;
public interface IElementToFieldDom extends Function<Element, Result<ElementFail, FieldDom>> {
    static IElementToFieldDom forEntity(LoggerAdapter loggerAdapter, ElementToBundle bundle, EntityNames entityNames) {return new SimpleElementToFieldDomForEntity(loggerAdapter, bundle.serverNames(), entityNames);}
    static IElementToFieldDom forView(LoggerAdapter loggerAdapter, ElementToBundle bundle, ViewNames viewNames) {return new SimpleElementToFieldDomForViews(loggerAdapter, bundle.serverNames(), viewNames);}

}

@RequiredArgsConstructor
abstract class AbstractElementToFieldDom implements IElementToFieldDom {
    final LoggerAdapter loggerAdapter;
    final IServerNames serverNames;
    abstract String findLensName(String fieldName, String annotationField);
    abstract String findLensPath(String fieldName, String annotationField);
    @Override public Result<ElementFail, FieldDom> apply(Element element) {
        String fieldType = element.asType().toString();
        String fieldName = element.getSimpleName().toString();
        Result<String, TypeDom> typeDom = TypeDom.create(serverNames, fieldType);
        return ElementFail.lift(element, typeDom).flatMap(td -> {
            Field annotation = element.getAnnotation(Field.class);

//            String defaultLensName = findLensName(fieldName, annotation.)
            String lensName = findLensName(fieldName, Optional.ofNullable(annotation).map(Field::lensName).orElse(""));
            String lensPath = findLensPath(fieldName, Optional.ofNullable(annotation).map(Field::lensPath).orElse(""));
//            String lensPath = findLensPath(fieldName, Optional.ofNullable(annotation.lensPath()).orElse(""));
            Boolean readOnly = Optional.ofNullable(annotation).map(Field::readOnly).orElse(false);
            String javascriptBody = Strings.from(Optional.ofNullable(annotation).map(Field::javascript).orElse(""), "return lens('" + lensPath + "');");
            String javascript = "function " + lensName + "(){" + javascriptBody + "};";
            loggerAdapter.info(element, fieldName + ": " + javascriptBody + "/" + javascript);
            Boolean templated = Optional.ofNullable(annotation).map(a -> a.templated()).orElse(false);
            Boolean deprecated = element.getAnnotation(Deprecated.class) != null;
            return Result.succeed(new FieldDom(td, fieldName, readOnly, lensName, lensPath, javascript, templated, deprecated));
        });
    }
}
class SimpleElementToFieldDomForEntity extends AbstractElementToFieldDom {
    final EntityNames entityNames;
    public SimpleElementToFieldDomForEntity(LoggerAdapter loggerAdapter, IServerNames serverNames, EntityNames entityNames) {
        super(loggerAdapter, serverNames);
        this.entityNames = entityNames;
    }
    @Override String findLensName(String fieldName, String annotationField) {
        return serverNames.entityLensName(entityNames, fieldName, annotationField);
    }
    @Override String findLensPath(String fieldName, String annotationField) {
        return serverNames.entityLensPath(entityNames, fieldName, annotationField);
    }
}

class SimpleElementToFieldDomForViews extends AbstractElementToFieldDom {
    final ViewNames viewNames;

    public SimpleElementToFieldDomForViews(LoggerAdapter loggerAdapter, IServerNames serverNames, ViewNames viewNames) {
        super(loggerAdapter, serverNames);
        this.viewNames = viewNames;
    }
    @Override String findLensName(String fieldName, String annotationField) {
        return "lens for views not done yet";
    }
    @Override String findLensPath(String fieldName, String annotationField) {
        return "lens path for views not done yet";
    }
}

