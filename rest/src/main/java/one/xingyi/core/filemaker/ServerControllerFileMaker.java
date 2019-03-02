package one.xingyi.core.filemaker;
import lombok.RequiredArgsConstructor;
import one.xingyi.core.annotationProcessors.ActionsDom;
import one.xingyi.core.codeDom.PackageAndClassName;
import one.xingyi.core.codeDom.ResourceDom;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.monad.MonadDefn;
import one.xingyi.core.utils.Formating;
import one.xingyi.core.utils.IdAndValue;
import one.xingyi.core.utils.Lists;
import one.xingyi.core.utils.Optionals;
import one.xingyi.core.validation.Result;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ServerControllerFileMaker implements IFileMaker<ResourceDom> {

    final MonadDefn monadDefn;

    List<String> addFromActionsDom(String result, ActionsDom actionsDom) {
        return Lists.append(
                Optionals.toList(actionsDom.putDom, dom -> "" + monadDefn.simpleClassName() + "<" + result + "> put(IdAndValue<" + result + "> idAnd" + result + ");"),
                Optionals.toList(actionsDom.getDom, dom -> monadDefn.simpleClassName() + "<" + result + "> get(String id);"),
                Optionals.toList(actionsDom.optionalGetDom, dom -> monadDefn.simpleClassName() + "<Optional<" + result + ">> getOptional(String id);"),
                Optionals.toList(actionsDom.deleteDom, dom -> "" + monadDefn.simpleClassName() + "<Boolean> delete(String id);"),
                Optionals.toList(actionsDom.createDom, dom -> "" + monadDefn.simpleClassName() + "<" + result + "> createWithId(String id);"),
                Optionals.toList(actionsDom.createWithoutIdDom, dom -> "" + monadDefn.simpleClassName() + "<IdAndValue<" + result + ">> createWithoutId(" + result + " value);"),
                Optionals.toList(actionsDom.createWithoutIdDom, dom -> result + " createWithoutIdRequestFrom(ServiceRequest serviceRequest);"),
                Lists.map(actionsDom.postDoms, pd -> "" + monadDefn.simpleClassName() + "<" + result + "> " + pd.action + "(String id); //" + pd.path + "    " + pd.states)
        );
    }

    @Override public Result<String, FileDefn> apply(ResourceDom resourceDom) {

        PackageAndClassName serverEntity = resourceDom.entityNames.serverEntity;
        List<String> manualImports = List.of(monadDefn.fullClassName(), serverEntity.asString());
        List<String> result = Lists.append(
                List.of("//" + resourceDom.bookmark),
                Formating.javaFile(getClass(), resourceDom.deprecated, resourceDom.entityNames.originalDefn, "interface", resourceDom.entityNames.serverController, "",
                        manualImports, IdAndValue.class, Optional.class, ServiceRequest.class),
                Formating.indent(List.of("String stateFn(" + serverEntity.className + " entity);")),
                Formating.indent(addFromActionsDom(serverEntity.className, resourceDom.actionsDom)),
                List.of("}"));
        return Result.succeed(new FileDefn(resourceDom.entityNames.serverController, Lists.join(result, "\n")));

    }
}
