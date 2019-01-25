package one.xingyi.core.filemaker;
import one.xingyi.core.codeDom.EntityDom;
import one.xingyi.core.utils.Formating;
import one.xingyi.core.utils.Lists;
import one.xingyi.core.validation.Result;

import java.util.List;
public class ServerControllerFileMaker implements IFileMaker<EntityDom> {
    @Override public Result<String, FileDefn> apply(EntityDom entityDom) {

        List<String> manualImports = List.of();
        List<String> result = Lists.append(
                Formating.javaFile(getClass(), entityDom.entityNames.originalDefn, "class", entityDom.entityNames.serverController, "", manualImports),
                List.of("}"));
        return Result.succeed(new FileDefn(entityDom.entityNames.serverController, Lists.join(result, "\n")));

    }
}
