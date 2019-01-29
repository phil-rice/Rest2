package one.xingyi.core.filemaker;
import one.xingyi.core.annotations.XingYiGenerated;
import one.xingyi.core.codeDom.EntityDom;
import one.xingyi.core.codeDom.FieldDom;
import one.xingyi.core.codeDom.FieldListDom;
import one.xingyi.core.sdk.IXingYiEntity;
import one.xingyi.core.utils.Formating;
import one.xingyi.core.utils.Lists;
import one.xingyi.core.validation.Result;

import java.util.ArrayList;
import java.util.List;
public class ServerInterfaceFileMaker implements IFileMaker<EntityDom> {

    List<String> allFieldsAccessors(String entityName, FieldListDom dom) { return dom.noDeprecatedflatMap(fd -> accessors(entityName, fd)); }

    List<String> accessors(String entityName, FieldDom dom) {
        List<String> result = new ArrayList<>();
        result.add("//" + dom.typeDom);
        result.add(dom.typeDom.forEntity() + " " + dom.name + "();");
        if (!dom.readOnly) {
            result.add(entityName + " with" + dom.name + "(" + dom.typeDom.forEntity() + " " + dom.name + ");");
        }
        return result;
    }

    @Override public Result<String, FileDefn> apply(EntityDom entityDom) {
        List<String> manualImports = Lists.unique(entityDom.fields.noDeprecatedmap(fd -> fd.typeDom.fullTypeName()));
        String result = Lists.join(Lists.append(
                Formating.javaFile(getClass(),entityDom.entityNames.originalDefn,"interface", entityDom.entityNames.serverInterface,
                        " extends IXingYiEntity", manualImports, IXingYiEntity.class, XingYiGenerated.class),
                Formating.indent(allFieldsAccessors(entityDom.entityNames.serverInterface.className, entityDom.fields)),
                List.of("}")
        ), "\n");
        return Result.succeed(new FileDefn(entityDom.entityNames.serverInterface, result));
    }
}
