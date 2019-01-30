package one.xingyi.core.filemaker;
import one.xingyi.core.codeDom.EntityDom;
import one.xingyi.core.codeDom.PackageAndClassName;
import one.xingyi.core.utils.Formating;
import one.xingyi.core.utils.Lists;
import one.xingyi.core.validation.Result;

import java.util.List;
public class CodeDomDebugFileMaker extends AbstractDebugFileMaker implements IFileMaker<EntityDom> {

    @Override public Result<String, FileDefn> apply(EntityDom entityDom) {
        PackageAndClassName packageAndClassName = entityDom.entityNames.serverEntity.mapName(e -> e + "DebugInfo");
        List<String> result = Lists.append(
                Formating.javaFile(getClass(), entityDom.deprecated, entityDom.entityNames.originalDefn, "class", packageAndClassName, "", List.of()),
                List.of("/*"),
                entityDebugInfo(entityDom),
                actionsDomInfo(entityDom.actionsDom),
                List.of("*/}"));
        return Result.succeed(new FileDefn(packageAndClassName, Lists.join(result, "\n")));
    }
}
