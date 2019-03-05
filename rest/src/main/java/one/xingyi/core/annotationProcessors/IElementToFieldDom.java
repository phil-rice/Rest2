package one.xingyi.core.annotationProcessors;
import lombok.RequiredArgsConstructor;
import one.xingyi.core.annotations.Field;
import one.xingyi.core.codeDom.FieldDom;
import one.xingyi.core.codeDom.ResourceDom;
import one.xingyi.core.codeDom.ViewDom;
import one.xingyi.core.names.EntityNames;
import one.xingyi.core.names.IServerNames;
import one.xingyi.core.names.ViewNames;
import one.xingyi.core.typeDom.TypeDom;
import one.xingyi.core.utils.Function3;
import one.xingyi.core.utils.Lists;
import one.xingyi.core.utils.LoggerAdapter;
import one.xingyi.core.utils.Strings;
import one.xingyi.core.validation.Result;

import javax.lang.model.element.Element;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
public interface IElementToFieldDom extends Function3<Element, IViewDefnNameToViewName, Optional<ResourceDom>, Result<ElementFail, FieldDom>> {
    static IElementToFieldDom forEntity(LoggerAdapter loggerAdapter, ElementToBundle bundle, EntityNames entityNames) {return new SimpleElementToFieldDomForEntity(loggerAdapter, bundle.serverNames(), entityNames);}
    static IElementToFieldDom forView(LoggerAdapter loggerAdapter, ElementToBundle bundle, ViewNames viewNames) {return new SimpleElementToFieldDomForViews(loggerAdapter, bundle.serverNames(), viewNames);}

}

@RequiredArgsConstructor
abstract class AbstractElementToFieldDom implements IElementToFieldDom {
    final LoggerAdapter loggerAdapter;
    final IServerNames serverNames;
    abstract String findLensName(String fieldName, String annotationField);
    abstract Result<String, String> findLensPath(String fieldName, String annotationField);

    private List<String> validateViewField(Optional<ResourceDom> optResourceDom, FieldDom viewField) {
        if (optResourceDom.isEmpty()) return List.of();// very often an incremental compilation issue
        ResourceDom resourceDom = optResourceDom.get();
        Optional<FieldDom> optResourceField = Lists.find(resourceDom.fields.allFields, resourceField -> resourceField.name.equals(viewField.name));
        if (optResourceField.isEmpty())
            return List.of("Cannot find matching field in " + resourceDom.entityNames.originalDefn.asString() + " for " + viewField.name);
        FieldDom resourceField = optResourceField.get();
        if (!resourceField.typeDom.isAssignableFrom(viewField.typeDom))
            return List.of("Field " + viewField.name + " has type " + viewField.typeDom.fullTypeName() + " but in resource " + resourceDom.entityNames.originalDefn.asString() + " has type " + resourceField.typeDom.fullTypeName());
        return List.of();
    }


    @Override public Result<ElementFail, FieldDom> apply(Element element, IViewDefnNameToViewName viewNamesMap, Optional<ResourceDom> resourceDoms) {
        String fieldType = element.asType().toString();
        String fieldName = element.getSimpleName().toString();
        Result<String, TypeDom> typeDom = TypeDom.create(serverNames, fieldType, viewNamesMap);
        loggerAdapter.info("creating " + fieldName + " " + resourceDoms.isPresent());
        return ElementFail.lift(element, typeDom.flatMap(td -> {
            Field annotation = element.getAnnotation(Field.class);

//            String defaultLensName = findLensName(fieldName, annotation.)
            String lensName = findLensName(fieldName, Optional.ofNullable(annotation).map(Field::lensName).orElse(""));
            String rawLensPath = Optional.ofNullable(annotation).map(Field::lensPath).orElse("");
            loggerAdapter.info("rawlenspath  " + rawLensPath + " " + resourceDoms.isPresent());
            return findLensPath(td.lensDefn(fieldName), rawLensPath).flatMap(lensPath -> {
                loggerAdapter.info("madelenspath  " + lensPath + " " + resourceDoms.isPresent());
//            String lensPath = findLensPath(fieldName, Optional.ofNullable(annotation.lensPath()).orElse(""));
                Boolean readOnly = Optional.ofNullable(annotation).map(Field::readOnly).orElse(false);
                String javascriptBody = Strings.from(Optional.ofNullable(annotation).map(Field::javascript).orElse(""), "return lens('" + fieldName + "');");
                String javascript = "function " + lensName + "(){" + javascriptBody + "};";
//            loggerAdapter.info(element, fieldName + ": " + javascriptBody + "/" + defn);
                Boolean templated = Optional.ofNullable(annotation).map(a -> a.templated()).orElse(false);
                Boolean deprecated = element.getAnnotation(Deprecated.class) != null;
                FieldDom fieldDom = new FieldDom(td, fieldName, readOnly, lensName, lensPath, javascript, templated, deprecated);
                loggerAdapter.info("made fieldDom  " + fieldDom + " " + resourceDoms.isPresent());
                List<String> errors = validateViewField(resourceDoms, fieldDom);
//                loggerAdapter.info(element, getClass().getSimpleName() + ": Checking against " + resourceDoms + " errors " + errors);
                loggerAdapter.info("made errors for " + fieldName + " " + errors);
                if (errors.size() > 0) return Result.failwith(errors.toString());
                return Result.<String, FieldDom>succeed(fieldDom);
            });
        }));
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
    @Override Result<String, String> findLensPath(String fieldName, String annotationField) {
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
    @Override Result<String, String> findLensPath(String fieldName, String annotationField) {
        return Result.succeed("lens path for views not done yet");
    }
}

