package one.xingyi.core.filemaker;
import one.xingyi.core.annotations.XingYiGenerated;
import one.xingyi.core.codeDom.PackageAndClassName;
import one.xingyi.core.codeDom.ResourceDom;
import one.xingyi.core.sdk.IXingYiClientResource;
import one.xingyi.core.utils.Formating;
import one.xingyi.core.utils.Lists;
import one.xingyi.core.validation.Result;

import java.util.List;
public class ClientResourceFileMaker implements IFileMaker<ResourceDom> {


    @Override public Result<String, FileDefn> apply(ResourceDom resourceDom) {
        PackageAndClassName clientResourceName = resourceDom.entityNames.clientResource;
        String result = Lists.join(Lists.append(
                Formating.javaFile(getClass(), resourceDom.deprecated, clientResourceName, "interface", clientResourceName,
                        " extends IXingYiClientResource", List.of(),
                        IXingYiClientResource.class, XingYiGenerated.class),
                List.of("}")
        ), "\n");
        return Result.succeed(new FileDefn(clientResourceName, result));
    }
}
