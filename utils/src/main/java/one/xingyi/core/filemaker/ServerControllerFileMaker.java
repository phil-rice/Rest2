package one.xingyi.core.filemaker;
import one.xingyi.core.annotationProcessors.ActionsDom;
import one.xingyi.core.codeDom.EntityDom;
import one.xingyi.core.codeDom.PackageAndClassName;
import one.xingyi.core.utils.Formating;
import one.xingyi.core.utils.IdAndValue;
import one.xingyi.core.utils.Lists;
import one.xingyi.core.utils.Optionals;
import one.xingyi.core.validation.Result;

import java.util.List;
import java.util.concurrent.CompletableFuture;
public class ServerControllerFileMaker implements IFileMaker<EntityDom> {

    List<String> addFromActionsDom(String result, ActionsDom actionsDom) {
        return Lists.append(
                Optionals.toList(actionsDom.putDom, dom -> "CompletableFuture<" + result + "> put(IdAndValue<String> idAnd" + result + ");"),
                Optionals.toList(actionsDom.getDom, dom -> "CompletableFuture<" + result + "> get(String id);"),
                Optionals.toList(actionsDom.deleteDom, dom -> "CompletableFuture<Boolean> delete(String id);"),
                Optionals.toList(actionsDom.createWithoutIdDom, dom -> "CompletableFuture<" + result + "> create(String id);"),
                Optionals.toList(actionsDom.createDom, dom -> "CompletableFuture<IdAndValue<" + result + ">> create();"),
                Lists.map(actionsDom.postDoms, pd -> "CompletableFuture<" + result + "> " + pd.action + "(String id); //" + pd.path + "    " +pd.states)
        );

        //    CompletableFuture<TrafficLights> put(String id, TrafficLights ColourView);
        //    CompletableFuture<TrafficLights> get(String id);
        //    CompletableFuture<Boolean> delete(String id);
        //    CompletableFuture<TrafficLights> createWithid(String id, TrafficLights ColourView);
        //    CompletableFuture<IdAndValue<TrafficLights>> apply(TrafficLights trafficLights);
        //    CompletableFuture<TrafficLights> changeOrange(String id);
        //    CompletableFuture<TrafficLights> changeGreen(String id);
        //    CompletableFuture<TrafficLights> changeRed(String id);
        //    CompletableFuture<TrafficLights> changeFlashing(String id);
    }

    @Override public Result<String, FileDefn> apply(EntityDom entityDom) {

        PackageAndClassName serverEntity = entityDom.entityNames.serverEntity;
        List<String> manualImports = List.of(serverEntity.asString());
        List<String> result = Lists.append(
                List.of("//" + entityDom.bookmark),
                Formating.javaFile(getClass(), entityDom.entityNames.originalDefn, "interface", entityDom.entityNames.serverController, "",
                        manualImports, CompletableFuture.class, IdAndValue.class),
                Formating.indent(addFromActionsDom(serverEntity.className, entityDom.actionsDom)),
                List.of("}"));
        return Result.succeed(new FileDefn(entityDom.entityNames.serverController, Lists.join(result, "\n")));

    }
}
