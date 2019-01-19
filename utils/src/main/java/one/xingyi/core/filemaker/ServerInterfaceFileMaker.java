package one.xingyi.core.filemaker;
import one.xingyi.core.annotations.XingYiGenerated;
import one.xingyi.core.codeDom.EntityDom;
import one.xingyi.core.codeDom.FieldDom;
import one.xingyi.core.codeDom.FieldListDom;
import one.xingyi.core.sdk.IXingYiEntity;
import one.xingyi.core.utils.Formating;
import one.xingyi.core.utils.Lists;
import one.xingyi.core.utils.Strings;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
public class ServerInterfaceFileMaker implements IFileMaker<EntityDom> {

    List<String> allFieldsAccessors(String entityName, FieldListDom dom) { return dom.flatMap(fd -> accessors(entityName, fd)); }

    List<String> accessors(String entityName, FieldDom dom) {
        List<String> result = new ArrayList<>();
        result.add("//" + dom.typeDom);
        result.add(dom.typeDom.fullTypeName() + " " + dom.name + "();");
        if (!dom.readOnly) {
            result.add(entityName + " with" + dom.name + "(" + dom.typeDom.fullTypeName() + " " + dom.name + ");");
        }
        return result;
    }

    @Override public FileDefn apply(EntityDom entityDom) {
        List<String> manualImports = Lists.unique(entityDom.fields.map(fd -> fd.typeDom.fullTypeName()));
        String result = Lists.join(Lists.append(
                Formating.javaFile("interface", entityDom.entityName.serverInterface,
                        " extends IXingYiEntity", manualImports, IXingYiEntity.class, XingYiGenerated.class),
                Formating.indent(allFieldsAccessors(entityDom.entityName.serverInterface.className, entityDom.fields)),
                List.of("}")
        ), "\n");
        return new FileDefn(entityDom.entityName.serverInterface, result);
    }
}
