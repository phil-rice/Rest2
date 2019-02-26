package one.xingyi.core.filemaker;
import one.xingyi.core.ISimpleMap;
import one.xingyi.core.annotations.XingYiGenerated;
import one.xingyi.core.client.IResourceList;
import one.xingyi.core.client.IXingYi;
import one.xingyi.core.codeDom.CompositeViewDom;
import one.xingyi.core.optics.Lens;
import one.xingyi.core.sdk.IXingYiClientImpl;
import one.xingyi.core.sdk.IXingYiCompositeCompanion;
import one.xingyi.core.utils.Formating;
import one.xingyi.core.utils.Lists;
import one.xingyi.core.validation.Result;

import java.util.List;
public class CompositeViewCompanionMaker implements IFileMaker<CompositeViewDom> {
    @Override public Result<String, FileDefn> apply(CompositeViewDom dom) {
        List<String> manualImports = List.of();
        String result = Lists.<String>join(Lists.<String>append(
                Formating.javaFile(getClass(), false, dom.originalDefn, "class", dom.clientCompositeCompanion,
                        " implements IXingYiCompositeCompanion<" +
                                dom.clientResource.asString() + "," +
                                dom.originalDefn.asString() + "," +
                                dom.clientImpl.asString() + ">",
                        manualImports, IXingYi.class, IXingYiClientImpl.class, XingYiGenerated.class, IXingYiCompositeCompanion.class,
                        IResourceList.class, Lens.class, ISimpleMap.class),
//                List.of(Formating.indent + "static public " + viewDom.viewNames.clientCompositeCompanion.asString() + " companion = " + viewDom.viewNames.clientCompositeCompanion.asString() + ".companion;"),
//                Formating.indent(fields(dom)),
//                List.of(Formating.indent + "@Override public Object mirror(){return mirror;}"),
//                List.of(Formating.indent + "@Override public " + xingyiDefn(dom) + " xingYi(){return xingYi;}"),
//                Formating.indent(constructor(dom)),
//                Formating.indent(allFieldAccessorsForView(viewDom.viewNames.clientEntity, viewDom.viewNames.clientView.className, dom.viewDomAndResourceDomFields)),
                Formating.indent(methods(dom)),
                List.of("}")
        ), "\n");
        return Result.succeed(new FileDefn(dom.clientCompositeCompanion, result));
    }
    private List<String> methods(CompositeViewDom dom) {
        return List.of(
                "@Override public " + dom.clientInterface.asString() + " make(IXingYi xingYi, Object mirror) { return new " +
                        dom.clientImpl.asString() + "(xingYi,mirror); }",
                "@Override public String acceptHeader() { return \"\"; }"
        );
    }
}
