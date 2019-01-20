package one.xingyi.core.filemaker;
import one.xingyi.core.annotations.XingYiGenerated;
import one.xingyi.core.client.IXingYi;
import one.xingyi.core.codeDom.*;
import one.xingyi.core.optics.Lens;
import one.xingyi.core.sdk.IXingYiClientImpl;
import one.xingyi.core.utils.Formating;
import one.xingyi.core.utils.Lists;
import one.xingyi.core.utils.Optionals;
import one.xingyi.core.utils.Strings;

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

        if (viewDom.typeDom.primitive()) {
            result.add("public Lens<" + interfaceName + "," + viewDom.typeDom.forView() + "> " + viewDom.name +
                    "Lens(){ return xingYi.stringLens(companion, " + Strings.quote(lensName) + ");}");
        } else {
            result.add("public Lens<" + interfaceName + "," + viewDom.typeDom.forView() + ">" +
                    viewDom.name + "Lens(){return xingYi.objectLens(companion, " + Strings.quote(lensName) + ");}");

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

    List<String> fields(FieldListDom fld) {
        return List.of("final IXingYi xingYi;", "final Object mirror;");
    }
    List<String> constructor(String classname, FieldListDom fld) {
        return List.of("public " + classname + "(IXingYi xingYi, Object mirror){", Formating.indent + "this.xingYi=xingYi;", Formating.indent + "this.mirror=mirror;", "}");
    }

    @Override public FileDefn apply(ViewDomAndItsEntityDom viewDomAndItsEntityDom) {
        ViewDom viewDom = viewDomAndItsEntityDom.viewDom;
        List<String> manualImports = Lists.unique(viewDom.fields.map(fd -> fd.typeDom.fullTypeName()));
        String result = Lists.join(Lists.append(
                Formating.javaFile(getClass(), "class", viewDom.viewNames.clientViewImpl,
                        " implements " + viewDom.viewNames.clientView.asString() + ",IXingYiClientImpl<" +
                                viewDom.viewNames.clientEntity.asString() + "," +
                                viewDom.viewNames.clientView.asString() + ">", manualImports, IXingYi.class, IXingYiClientImpl.class, XingYiGenerated.class, Lens.class),
                List.of(Formating.indent + "static public " + viewDom.viewNames.clientCompanion.asString() + " companion;"),
                Formating.indent(fields(viewDom.fields)),
                Formating.indent(constructor(viewDom.viewNames.clientViewImpl.className, viewDom.fields)),
                Formating.indent(allFieldAccessorsForView(viewDom.viewNames.clientEntity, viewDom.viewNames.clientView.className, viewDomAndItsEntityDom.viewAndEntityFields)),
                List.of("}")
        ), "\n");
        return new FileDefn(viewDom.viewNames.clientViewImpl, result);
    }
}