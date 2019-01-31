package one.xingyi.core.filemaker;
import one.xingyi.core.annotationProcessors.ActionsDom;
import one.xingyi.core.codeDom.ResourceDom;
import one.xingyi.core.codeDom.PackageAndClassName;
import one.xingyi.core.utils.Formating;
import one.xingyi.core.utils.IdAndValue;
import one.xingyi.core.utils.Lists;
import one.xingyi.core.utils.Optionals;
import one.xingyi.core.validation.Result;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
public class ServerControllerFileMaker implements IFileMaker<ResourceDom> {

    List<String> addFromActionsDom(String result, ActionsDom actionsDom) {
        return Lists.append(
                Optionals.toList(actionsDom.putDom, dom -> "CompletableFuture<" + result + "> put(IdAndValue<" + result + "> idAnd" + result + ");"),
                Optionals.toList(actionsDom.getDom, dom -> dom.mustExist ? "CompletableFuture<" + result + "> get(String id);" : "CompletableFuture<Optional<" + result + ">> getOptional(String id);"),
                Optionals.toList(actionsDom.deleteDom, dom -> "CompletableFuture<Boolean> delete(String id);"),
                Optionals.toList(actionsDom.createWithoutIdDom, dom -> "CompletableFuture<" + result + "> create(String id);"),
                Optionals.toList(actionsDom.createDom, dom -> "CompletableFuture<IdAndValue<" + result + ">> create();"),
                Lists.map(actionsDom.postDoms, pd -> "CompletableFuture<" + result + "> " + pd.action + "(String id); //" + pd.path + "    " + pd.states)
        );
    }

    @Override public Result<String, FileDefn> apply(ResourceDom resourceDom) {

        PackageAndClassName serverEntity = resourceDom.entityNames.serverEntity;
        List<String> manualImports = List.of(serverEntity.asString());
        List<String> result = Lists.append(
                List.of("//" + resourceDom.bookmark),
                Formating.javaFile(getClass(), resourceDom.deprecated, resourceDom.entityNames.originalDefn, "interface", resourceDom.entityNames.serverController, "",
                        manualImports, CompletableFuture.class, IdAndValue.class, Optional.class),
                Formating.indent(List.of("String stateFn(" + serverEntity.className + " entity);")),
                Formating.indent(addFromActionsDom(serverEntity.className, resourceDom.actionsDom)),
                List.of("}"));
        return Result.succeed(new FileDefn(resourceDom.entityNames.serverController, Lists.join(result, "\n")));

    }
}
