package one.xingyi.core.filemaker;
import one.xingyi.core.annotations.XingYiGenerated;
import one.xingyi.core.codeDom.ViewDom;
import one.xingyi.core.codeDom.ViewDomAndItsResourceDom;
import one.xingyi.core.sdk.IXingYiClientResource;
import one.xingyi.core.utils.Formating;
import one.xingyi.core.utils.Lists;
import one.xingyi.core.validation.Result;

import java.util.List;
public class ClientResourceFileMaker implements IFileMaker<ViewDomAndItsResourceDom> {


    @Override public Result<String, FileDefn> apply(ViewDomAndItsResourceDom viewAndEntityDom) {
        ViewDom viewDom = viewAndEntityDom.viewDom;
        String result = Lists.join(Lists.append(
                Formating.javaFile(getClass(), viewDom.deprecated, viewAndEntityDom.viewDom.viewNames.originalDefn, "interface", viewDom.viewNames.clientEntity,
                        " extends IXingYiClientResource", List.of(),
                        IXingYiClientResource.class, XingYiGenerated.class),
                List.of("}")
        ), "\n");
        return Result.succeed(new FileDefn(viewDom.viewNames.clientEntity, result));
    }
}
