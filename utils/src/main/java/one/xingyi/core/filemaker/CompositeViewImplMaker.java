package one.xingyi.core.filemaker;
import one.xingyi.core.ISimpleMap;
import one.xingyi.core.annotations.XingYiGenerated;
import one.xingyi.core.client.IResourceList;
import one.xingyi.core.client.IXingYi;
import one.xingyi.core.codeDom.*;
import one.xingyi.core.optics.Lens;
import one.xingyi.core.sdk.IXingYiClientImpl;
import one.xingyi.core.utils.Formating;
import one.xingyi.core.utils.Lists;
import one.xingyi.core.validation.Result;

import java.util.List;
public class CompositeViewImplMaker implements IFileMaker<CompositeViewDom> {


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
                Formating.javaFile(getClass(), false, dom.originalDefn, "class", dom.clientImpl,
                        " implements " + "IXingYiView<" + dom.clientResource.asString() + ">",
                         manualImports, IXingYi.class, IXingYiClientImpl.class, XingYiGenerated.class,
                        IResourceList.class, Lens.class, ISimpleMap.class),
//                List.of(Formating.indent + "static public " + viewDom.viewNames.clientCompanion.asString() + " companion = " + viewDom.viewNames.clientCompanion.asString() + ".companion;"),
//                Formating.indent(fields(resourceDom, viewDom)),
//                List.of(Formating.indent + "@Override public Object mirror(){return mirror;}"),
//                List.of(Formating.indent + "@Override public IXingYi<" + resourceDom.entityNames.clientResource.asString() + "," + viewDom.viewNames.clientView.asString() + "> xingYi(){return xingYi;}"),
//                Formating.indent(constructor(resourceDom, viewDom)),
//                Formating.indent(allFieldAccessorsForView(viewDom.viewNames.clientEntity, viewDom.viewNames.clientView.className, dom.viewDomAndResourceDomFields)),
                List.of("}")
        ), "\n");
        return Result.succeed(new FileDefn(dom.clientImpl, result));
    }
}
