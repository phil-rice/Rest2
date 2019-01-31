package one.xingyi.core.filemaker;
import one.xingyi.core.annotations.XingYiGenerated;
import one.xingyi.core.codeDom.ResourceDom;
import one.xingyi.core.codeDom.FieldDom;
import one.xingyi.core.codeDom.FieldListDom;
import one.xingyi.core.sdk.IXingYiResource;
import one.xingyi.core.utils.Formating;
import one.xingyi.core.utils.Lists;
import one.xingyi.core.validation.Result;

import java.util.ArrayList;
import java.util.List;
public class ServerInterfaceFileMaker implements IFileMaker<ResourceDom> {

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

    @Override public Result<String, FileDefn> apply(ResourceDom resourceDom) {
        List<String> manualImports = Lists.unique(resourceDom.fields.noDeprecatedmap(fd -> fd.typeDom.nested().fullTypeName()));
        String result = Lists.join(Lists.append(
                Formating.javaFile(getClass(), resourceDom.deprecated, resourceDom.entityNames.originalDefn, "interface", resourceDom.entityNames.serverInterface,
                        " extends IXingYiResource", manualImports, IXingYiResource.class, XingYiGenerated.class),
                Formating.indent(allFieldsAccessors(resourceDom.entityNames.serverInterface.className, resourceDom.fields)),
                List.of("}")
        ), "\n");
        return Result.succeed(new FileDefn(resourceDom.entityNames.serverInterface, result));
    }
}
