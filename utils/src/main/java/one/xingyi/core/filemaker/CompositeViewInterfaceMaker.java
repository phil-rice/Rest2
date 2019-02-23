package one.xingyi.core.filemaker;
import one.xingyi.core.ISimpleMap;
import one.xingyi.core.annotations.XingYiGenerated;
import one.xingyi.core.client.IResourceList;
import one.xingyi.core.client.IXingYi;
import one.xingyi.core.codeDom.CompositeViewDom;
import one.xingyi.core.codeDom.FieldListDom;
import one.xingyi.core.codeDom.ResourceDom;
import one.xingyi.core.codeDom.ViewDom;
import one.xingyi.core.monad.CompletableFutureDefn;
import one.xingyi.core.monad.MonadDefn;
import one.xingyi.core.optics.Lens;
import one.xingyi.core.sdk.IXingYiClientImpl;
import one.xingyi.core.utils.Formating;
import one.xingyi.core.utils.Lists;
import one.xingyi.core.validation.Result;

import java.util.List;
public class CompositeViewInterfaceMaker implements IFileMaker<CompositeViewDom>, CreateViewMethods {


    List<String> fields(ResourceDom resourceDom, ViewDom viewDom) {
        return List.of("final IXingYi<" + resourceDom.entityNames.clientResource.asString() + "," + viewDom.viewNames.clientView.asString() + "> xingYi;", "final Object mirror;");
    }
    List<String> constructor(ResourceDom resourceDom, ViewDom viewDom) {
        String classname = viewDom.viewNames.clientViewImpl.className;
        FieldListDom fld = viewDom.fields;
        return List.of("public " + classname + "(IXingYi<" + resourceDom.entityNames.clientResource.asString() + "," + viewDom.viewNames.clientView.asString() + ">xingYi, Object mirror){", Formating.indent + "this.xingYi=xingYi;", Formating.indent + "this.mirror=mirror;", "}");
    }

    @Override public Result<String, FileDefn> apply(CompositeViewDom dom) {
        List<String> manualImports = List.of();
        String result = Lists.<String>join(Lists.<String>append(
                Formating.javaFile(getClass(), false, dom.originalDefn, "interface", dom.clientInterface,
                        "",
                        manualImports, IXingYi.class, IXingYiClientImpl.class, XingYiGenerated.class,
                        IResourceList.class, Lens.class, ISimpleMap.class),
//                Formating.indent(getRemoteAccessors(dom.clientInterface.asString(), dom.companion, bookmarkUrlAndActionsDom)),
                List.of("}")
        ), "\n");
        return Result.succeed(new FileDefn(dom.clientInterface, result));
    }
    @Override public MonadDefn monadDefn() {
        return new CompletableFutureDefn();
    }
}
