package one.xingyi.core.filemaker;
import one.xingyi.core.annotations.XingYiGenerated;
import one.xingyi.core.codeDom.PackageAndClassName;
import one.xingyi.core.codeDom.ResourceDom;
import one.xingyi.core.sdk.IXingYiClientResource;
import one.xingyi.core.utils.Formating;
import one.xingyi.core.utils.Lists;
import one.xingyi.core.utils.Strings;
import one.xingyi.core.validation.Result;

import java.util.List;
public class ClientResourceFileMaker implements IFileMaker<ResourceDom> {


    @Override public Result<String, FileDefn> apply(ResourceDom resourceDom) {
        PackageAndClassName clientResourceName = resourceDom.entityNames.clientResource;
        String result = Lists.join(Lists.append(
                Formating.javaFile(getClass(), resourceDom.deprecated, clientResourceName, "interface", clientResourceName,
                        " extends IXingYiClientResource", List.of(),
                        IXingYiClientResource.class, XingYiGenerated.class),
                Formating.indent(addBookmark(resourceDom)),
                Formating.indent(addPrototypeId(resourceDom)),
                List.of("}")
        ), "\n");
        return Result.succeed(new FileDefn(clientResourceName, result));
    }
    private List<String> addBookmark(ResourceDom resourceDom) {
        if (resourceDom.bookmark.isEmpty()) return List.of();
        return List.of("final String bookmark=" + Strings.quote(resourceDom.bookmark.get().bookmark) + ";");
    }
    private List<String> addPrototypeId(ResourceDom resourceDom) {
        if (resourceDom.actionsDom.prototypeDom.isEmpty()) return List.of();
        return List.of("final String prototypeId=" + Strings.quote(resourceDom.actionsDom.prototypeDom.get().prototypeId) + ";");
    }
}

