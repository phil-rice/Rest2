package one.xingyi.core.filemaker;
import one.xingyi.core.annotations.XingYiGenerated;
import one.xingyi.core.codeDom.FieldDom;
import one.xingyi.core.codeDom.FieldListDom;
import one.xingyi.core.codeDom.ViewDom;
import one.xingyi.core.sdk.IXingYiClientImpl;
import one.xingyi.core.sdk.IXingYiView;
import one.xingyi.core.utils.Formating;
import one.xingyi.core.utils.Lists;

import java.util.ArrayList;
import java.util.List;
public class ClientViewImplFileMaker implements IFileMaker<ViewDom> {

    List<String> allFieldsAccessors(String interfaceName, FieldListDom dom) { return dom.flatMap(fd -> accessors(interfaceName, fd)); }

    List<String> accessors(String interfaceName, FieldDom dom) {
        List<String> result = new ArrayList<>();
        result.add("//" + dom.typeDom);
        result.add("public "+ dom.typeDom.transformed() + " " + dom.name + "(){return null;};");
        if (!dom.readOnly) {
            result.add("public "+ interfaceName + " with" + dom.name + "(" + dom.typeDom.transformed() + " " + dom.name + "){return null;};");
        }
        return result;
    }

    @Override public FileDefn apply(ViewDom viewDom) {
        List<String> manualImports = Lists.unique(viewDom.fields.map(fd -> fd.typeDom.fullTypeName()));
        String result = Lists.join(Lists.append(
                Formating.javaFile("class", viewDom.viewNames.clientViewImpl,
                        " implements " + viewDom.viewNames.clientView.asString() + ",IXingYiClientImpl<" +
                                viewDom.viewNames.clientEntity.asString() + ","+
                                viewDom.viewNames.clientView.asString() + ">", manualImports, IXingYiClientImpl.class, XingYiGenerated.class),
                Formating.indent(allFieldsAccessors(viewDom.viewNames.clientView.className, viewDom.fields)),
                List.of("}")
        ), "\n");
        return new FileDefn(viewDom.viewNames.clientViewImpl, result);
    }
}
