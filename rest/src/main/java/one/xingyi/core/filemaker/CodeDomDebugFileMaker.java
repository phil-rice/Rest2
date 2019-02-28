package one.xingyi.core.filemaker;
import one.xingyi.core.codeDom.ResourceDom;
import one.xingyi.core.codeDom.PackageAndClassName;
import one.xingyi.core.utils.Formating;
import one.xingyi.core.utils.Lists;
import one.xingyi.core.validation.Result;

import java.util.List;
public class CodeDomDebugFileMaker extends AbstractDebugFileMaker implements IFileMaker<ResourceDom> {

    @Override public Result<String, FileDefn> apply(ResourceDom resourceDom) {
        PackageAndClassName packageAndClassName = resourceDom.entityNames.serverEntity.mapName(e -> e + "DebugInfo");
        List<String> result = Lists.append(
                Formating.javaFile(getClass(), resourceDom.deprecated, resourceDom.entityNames.originalDefn, "class", packageAndClassName, "", List.of()),
                List.of("/*"),
                entityDebugInfo(resourceDom),
                actionsDomInfo(resourceDom.actionsDom),
                List.of("*/}"));
        return Result.succeed(new FileDefn(packageAndClassName, Lists.join(result, "\n")));
    }
}
