package one.xingyi.core.filemaker;
import one.xingyi.core.ISimpleMap;
import one.xingyi.core.annotations.XingYiGenerated;
import one.xingyi.core.client.ISimpleList;
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
public class ClientViewImplFileMaker implements IFileMaker<ViewDomAndItsEntityDom> {


    List<String> viewAndEntityaccessors(PackageAndClassName clientEntity, String interfaceName, ViewDomAndEntityDomField viewDomAndEntityDomField) {
        FieldDom viewDom = viewDomAndEntityDomField.viewDomField;
        Optional<FieldDom> entityDom = viewDomAndEntityDomField.entityDomField;
        List<String> result = new ArrayList<>();
        result.add("//View" + viewDomAndEntityDomField.viewDomField);
        result.add("//Entity" + viewDomAndEntityDomField.entityDomField);
        String lensName = entityDom.map(fd -> fd.lensName).orElse("not defined. Is this because of incremental compilation?");

        //TODO wow... really ugly
        if (viewDom.typeDom.primitive()) {
            result.add("public Lens<" + interfaceName + "," + viewDom.typeDom.forView() + "> " + viewDom.name +
                    "Lens(){ return xingYi.stringLens(companion, " + Strings.quote(lensName) + ");}");
        } else if (viewDom.typeDom instanceof ListType) {
            result.add("//" + viewDom.typeDom);
            result.add("public Lens<" + interfaceName + "," + viewDom.typeDom.forView() + ">" +
                    viewDom.name + "Lens(){return xingYi.listLens(companion, " + viewDom.typeDom.nested().viewCompanion() + ".companion," + Strings.quote(lensName) + ");}");

        } else {
            result.add("public Lens<" + interfaceName + "," + viewDom.typeDom.forView() + ">" +
                    viewDom.name + "Lens(){return xingYi.objectLens(companion, " + viewDom.typeDom.nested().viewCompanion() + ".companion," + Strings.quote(lensName) + ");}");

        }
        result.add("public " + viewDom.typeDom.forView() + " " + viewDom.name + "(){ return " + viewDom.name + "Lens().get(this);};");
        if (!viewDom.readOnly && entityDom.map(f -> !f.readOnly).orElse(true)) {
            result.add("public " + interfaceName + " with" + viewDom.name + "(" +
                    viewDom.typeDom.forView() + " " + viewDom.name + "){ return " + viewDom.name + "Lens().set(this," + viewDom.name + ");}");
        }
        return result;
    }

    List<String> allFieldAccessorsForView(PackageAndClassName clientEntity, String interfaceName, List<ViewDomAndEntityDomField> fields) {
        return Lists.flatMap(fields, f -> viewAndEntityaccessors(clientEntity, interfaceName, f));
    }

    List<String> fields(ViewDom viewDom) {
        return List.of("final IXingYi<" + viewDom.viewNames.clientEntity.asString() + "," + viewDom.viewNames.clientView.asString() + "> xingYi;", "final Object mirror;");
    }
    List<String> constructor(ViewDom viewDom) {
        String classname = viewDom.viewNames.clientViewImpl.className;
        FieldListDom fld = viewDom.fields;
        return List.of("public " + classname + "(IXingYi<" + viewDom.viewNames.clientEntity.asString() + "," + viewDom.viewNames.clientView.asString() + ">xingYi, Object mirror){", Formating.indent + "this.xingYi=xingYi;", Formating.indent + "this.mirror=mirror;", "}");
    }

    @Override public Result<String, FileDefn> apply(ViewDomAndItsEntityDom viewDomAndItsEntityDom) {
        ViewDom viewDom = viewDomAndItsEntityDom.viewDom;
        List<String> manualImports = Lists.unique(viewDom.fields.withDeprecatedmap(fd -> fd.typeDom.nested().fullTypeName()));
        String result = Lists.<String>join(Lists.<String>append(
                Formating.javaFile(getClass(), viewDom.deprecated, viewDom.viewNames.originalDefn, "class", viewDom.viewNames.clientViewImpl,
                        " implements " + viewDom.viewNames.clientView.asString() + ",IXingYiClientImpl<" +
                                viewDom.viewNames.clientEntity.asString() + "," +
                                viewDom.viewNames.clientView.asString() + ">", manualImports, IXingYi.class, IXingYiClientImpl.class, XingYiGenerated.class,
                        ISimpleList.class, Lens.class, ISimpleMap.class),
                List.of(Formating.indent + "static public " + viewDom.viewNames.clientCompanion.asString() + " companion = " + viewDom.viewNames.clientCompanion.asString() + ".companion;"),
                Formating.indent(fields(viewDom)),
                List.of(Formating.indent + "@Override public Object mirror(){return mirror;}"),
                List.of(Formating.indent + "@Override public IXingYi<" + viewDom.viewNames.clientEntity.asString() + "," + viewDom.viewNames.clientView.asString() + "> xingYi(){return xingYi;}"),
                Formating.indent(constructor(viewDom)),
                Formating.indent(allFieldAccessorsForView(viewDom.viewNames.clientEntity, viewDom.viewNames.clientView.className, viewDomAndItsEntityDom.viewAndEntityFields)),
                List.of("}")
        ), "\n");
        return Result.succeed(new FileDefn(viewDom.viewNames.clientViewImpl, result));
    }
}
