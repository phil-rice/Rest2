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
        Optional<String> lensName = entityDom.map(fd -> fd.lensName);
        String getterBody = Optionals.fold(lensName,
                () -> "throw new RuntimeException(" + Strings.quote("Cannot find lensname for field " + viewDom.name + ")"),
                ln -> "return xingYi.lens(" + Strings.quote(ln) + ")");
        result.add("//viewTypedom:    " + viewDom.typeDom);
        result.add("//entityTypeDom:   " + entityDom.map(x -> x.typeDom));

        result.add("public Lens<" + interfaceName + "," + viewDom.typeDom.forView() + "> " + viewDom.name +
                "Lens(){ return xingYi.<" + clientEntity.asString() + "," +
                interfaceName + ">stringLens(" + interfaceName + "::new, " + Strings.quote(entityDom.map(e -> e.lensName).orElse("not known")) + ");})");
//        result.add("public Lens<" + interfaceName + "," + viewDom.typeDom.forView() + "> " + viewDom.name +
//                "Lens(){ return xingYi.<" + viewDom.typeDom.clientEntity().orElse("not a view") + "<" +
//                interfaceName + ">stringLens(" + interfaceName + "::new, " + Strings.quote(entityDom.map(e -> e.lensName).orElse("not known")) + ");})");
        result.add("public " + viewDom.typeDom.forView() + " " + viewDom.name + "(){" + getterBody + ";};");
        if (!viewDom.readOnly && entityDom.map(f -> !f.readOnly).orElse(true)) {
            result.add("public " + interfaceName + " with" + viewDom.name + "(" + viewDom.typeDom.forView() + " " + viewDom.name + "){return null;}");
        }
        return result;
    }

    List<String> allFieldAccessorsForView(PackageAndClassName clientEntity, String interfaceName, List<ViewDomAndEntityDomField> fields) {
        return Lists.flatMap(fields, f -> viewAndEntityaccessors(clientEntity,interfaceName, f));
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
                Formating.javaFile("class", viewDom.viewNames.clientViewImpl,
                        " implements " + viewDom.viewNames.clientView.asString() + ",IXingYiClientImpl<" +
                                viewDom.viewNames.clientEntity.asString() + "," +
                                viewDom.viewNames.clientView.asString() + ">", manualImports, IXingYi.class, IXingYiClientImpl.class, XingYiGenerated.class, Lens.class),
                Formating.indent(fields(viewDom.fields)),
                Formating.indent(constructor(viewDom.viewNames.clientViewImpl.className, viewDom.fields)),
                Formating.indent(allFieldAccessorsForView(viewDom.viewNames.entityNames.clientEntity,viewDom.viewNames.clientView.className, viewDomAndItsEntityDom.viewAndEntityFields)),
                List.of("}")
        ), "\n");
        return new FileDefn(viewDom.viewNames.clientViewImpl, result);
    }
}
