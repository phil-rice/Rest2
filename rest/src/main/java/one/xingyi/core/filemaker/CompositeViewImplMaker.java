package one.xingyi.core.filemaker;
import one.xingyi.core.ISimpleMap;
import one.xingyi.core.annotations.XingYiGenerated;
import one.xingyi.core.client.IResourceList;
import one.xingyi.core.client.IXingYi;
import one.xingyi.core.codeDom.*;
import one.xingyi.core.optics.Lens;
import one.xingyi.core.sdk.IXingYiClientImpl;
import one.xingyi.core.sdk.IXingYiCompositeImpl;
import one.xingyi.core.utils.Formating;
import one.xingyi.core.utils.Lists;
import one.xingyi.core.validation.Result;

import java.util.List;
public class CompositeViewImplMaker implements IFileMaker<CompositeViewDom> {

    String xingyiDefn(CompositeViewDom dom) {
        return "IXingYi<" + dom.clientResource.asString() + "," + dom.clientInterface.asString() + ">";
    }
    List<String> fields(CompositeViewDom dom) {
        return List.of("final " + xingyiDefn(dom) + " xingYi;", "final Object mirror;");
    }
    List<String> constructor(CompositeViewDom dom) {
        return List.of("public " + dom.clientImpl.className + "(" + xingyiDefn(dom) + "xingYi, Object mirror){",
                Formating.indent + "this.xingYi=xingYi;",
                Formating.indent + "this.mirror=mirror;",
                "}");
    }

    @Override public Result<String, FileDefn> apply(CompositeViewDom dom) {
        List<String> manualImports = List.of();
        String result = Lists.<String>join(Lists.<String>append(
                Formating.javaFile(getClass(), false, dom.originalDefn, "class", dom.clientImpl,
                        " implements IXingYiCompositeImpl<" + dom.clientResource.asString()  + ">," + dom.clientInterface.asString(),
                        manualImports, IXingYi.class, IXingYiClientImpl.class, XingYiGenerated.class,
                        IResourceList.class, Lens.class, ISimpleMap.class, IXingYiCompositeImpl.class),
//                List.of(Formating.indent + "static public " + viewDom.viewNames.clientCompositeCompanion.asString() + " companion = " + viewDom.viewNames.clientCompositeCompanion.asString() + ".companion;"),
                Formating.indent(fields(dom)),
                List.of(Formating.indent + "@Override public Object mirror(){return mirror;}"),
                List.of(Formating.indent + "@Override public " + xingyiDefn(dom) + " xingYi(){return xingYi;}"),
                Formating.indent(constructor(dom)),
//                Formating.indent(allFieldAccessorsForView(viewDom.viewNames.clientEntity, viewDom.viewNames.clientView.className, dom.viewDomAndResourceDomFields)),
                List.of("}")
        ), "\n");
        return Result.succeed(new FileDefn(dom.clientImpl, result));
    }
}
