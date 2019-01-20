package one.xingyi.core.filemaker;
import one.xingyi.core.annotations.XingYiGenerated;
import one.xingyi.core.codeDom.*;
import one.xingyi.core.sdk.IXingYiView;
import one.xingyi.core.utils.Formating;
import one.xingyi.core.utils.Lists;

import java.util.ArrayList;
import java.util.List;
public class ClientViewInterfaceFileMaker implements IFileMaker<ViewDomAndItsEntityDom> {

    List<String> allFieldsAccessors(String interfaceName, List<ViewDomAndEntityDomField> dom) { return Lists.flatMap(dom, fd -> accessors(interfaceName, fd)); }

    List<String> accessors(String interfaceName, ViewDomAndEntityDomField viewDomAndEntityDom) {
        FieldDom dom = viewDomAndEntityDom.viewDomField;
        List<String> result = new ArrayList<>();
        result.add("//" + dom.typeDom);
        result.add(dom.typeDom.forView() + " " + dom.name + "();");
        if (!dom.readOnly && !viewDomAndEntityDom.entityDomField.map(e -> e.readOnly).orElse(false)) {
            result.add(interfaceName + " with" + dom.name + "(" + dom.typeDom.forView() + " " + dom.name + ");");
        }
        return result;
    }

    @Override public FileDefn apply(ViewDomAndItsEntityDom viewDomAndItsEntityDom) {
        ViewDom viewDom = viewDomAndItsEntityDom.viewDom;
        List<String> manualImports = Lists.unique(viewDom.fields.map(fd -> fd.typeDom.fullTypeName()));
        String result = Lists.join(Lists.append(
                Formating.javaFile(getClass(), "interface", viewDom.viewNames.clientView,
                        " extends IXingYiView<" + viewDom.viewNames.clientEntity.asString() + ">", manualImports, IXingYiView.class, XingYiGenerated.class),
                Formating.indent(allFieldsAccessors(viewDom.viewNames.clientView.className, viewDomAndItsEntityDom.viewAndEntityFields)),
                List.of("}")
        ), "\n");
        return new FileDefn(viewDom.viewNames.clientView, result);
    }
}