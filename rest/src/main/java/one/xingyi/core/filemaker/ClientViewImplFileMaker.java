package one.xingyi.core.filemaker;
import one.xingyi.core.ISimpleMap;
import one.xingyi.core.annotations.XingYiGenerated;
import one.xingyi.core.client.IResourceList;
import one.xingyi.core.client.IXingYi;
import one.xingyi.core.codeDom.*;
import one.xingyi.core.optics.Lens;
import one.xingyi.core.sdk.IXingYiClientImpl;
import one.xingyi.core.typeDom.ListType;
import one.xingyi.core.utils.Formating;
import one.xingyi.core.utils.Lists;
import one.xingyi.core.utils.Strings;
import one.xingyi.core.validation.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
public class ClientViewImplFileMaker implements IFileMaker<ViewDomAndItsResourceDom> {



    List<String> fields(ResourceDom resourceDom, ViewDom viewDom) {
        return List.of("final IXingYi<" + resourceDom.entityNames.clientResource.asString() + "," + viewDom.viewNames.clientView.asString() + "> xingYi;", "final Object mirror;");
    }
    List<String> constructor(ResourceDom resourceDom, ViewDom viewDom) {
        String classname = viewDom.viewNames.clientViewImpl.className;
        FieldListDom fld = viewDom.fields;
        return List.of("public " + classname + "(IXingYi<" + resourceDom.entityNames.clientResource.asString() + "," + viewDom.viewNames.clientView.asString() + ">xingYi, Object mirror){", Formating.indent + "this.xingYi=xingYi;", Formating.indent + "this.mirror=mirror;", "}");
    }

    @Override public Result<String, FileDefn> apply(ViewDomAndItsResourceDom viewDomAndItsResourceDom) {
        ViewDom viewDom = viewDomAndItsResourceDom.viewDom;
        if (viewDomAndItsResourceDom.entityDom.isEmpty()) return Result.failwith("could not create  view impl. Perhaps this is an incremental compilation issue and you need to do a full compile");
        ResourceDom resourceDom = viewDomAndItsResourceDom.entityDom.get();
        List<String> manualImports = Lists.unique(viewDom.fields.withDeprecatedmap(fd -> fd.typeDom.nested().fullTypeName()));
        String result = Lists.<String>join(Lists.<String>append(
                Formating.javaFile(getClass(), viewDom.deprecated, viewDom.viewNames.originalDefn, "class", viewDom.viewNames.clientViewImpl,
                        " implements " + viewDom.viewNames.clientView.asString() + ",IXingYiClientImpl<" +
                                resourceDom.entityNames.clientResource.asString() + "," +
                                viewDom.viewNames.clientView.asString() + ">", manualImports, IXingYi.class, IXingYiClientImpl.class, XingYiGenerated.class,
                        IResourceList.class, Lens.class, ISimpleMap.class),
                List.of(Formating.indent + "static public " + viewDom.viewNames.clientCompanion.asString() + " companion = " + viewDom.viewNames.clientCompanion.asString() + ".companion;"),
                Formating.indent(fields(resourceDom, viewDom)),
                List.of(Formating.indent + "@Override public Object mirror(){return mirror;}"),
                List.of(Formating.indent + "@Override public IXingYi<" + resourceDom.entityNames.clientResource.asString() + "," + viewDom.viewNames.clientView.asString() + "> xingYi(){return xingYi;}"),
                Formating.indent(constructor(resourceDom, viewDom)),
//                Formating.indent(allFieldAccessorsForView(viewDom.viewNames.clientEntity, viewDom.viewNames.clientView.className, viewDomAndItsResourceDom.viewDomAndResourceDomFields)),
                List.of("}")
        ), "\n");
        return Result.succeed(new FileDefn(viewDom.viewNames.clientViewImpl, result));
    }
}
