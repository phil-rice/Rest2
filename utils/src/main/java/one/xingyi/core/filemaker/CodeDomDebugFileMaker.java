package one.xingyi.core.filemaker;
import one.xingyi.core.codeDom.EntityDom;
import one.xingyi.core.codeDom.FieldDom;
import one.xingyi.core.codeDom.FieldListDom;
import one.xingyi.core.codeDom.PackageAndClassName;
import one.xingyi.core.names.EntityNames;
import one.xingyi.core.typeDom.TypeDom;
import one.xingyi.core.utils.Formating;
import one.xingyi.core.utils.Lists;

import java.util.Arrays;
import java.util.List;
public class CodeDomDebugFileMaker extends AbstracDebugFileMaker implements IFileMaker<EntityDom> {

    List<String> entityDebugInfo(EntityDom entityDom) {
        return Lists.<String>append(
                entityNameInfo(entityDom.entityName),
                List.of(
                        "bookmark " + entityDom.bookmark + ", geturl: " + entityDom.getUrl,
                        "Fields: " + entityDom.fields.allFields.size()
                ),
                Formating.indent(Lists.<FieldDom, String>flatMap(entityDom.fields.allFields, this::fieldDebugInfo))
        );
    }

    @Override public FileDefn apply(EntityDom entityDom) {
        PackageAndClassName packageAndClassName = entityDom.entityName.serverEntity.mapName(e -> e + "DebugInfo");
        List<String> result = Lists.append(
                Formating.javaFile("class", packageAndClassName, "", List.of()),
                List.of("/*"),
                entityDebugInfo(entityDom),
                List.of("*/}"));
        return new FileDefn(packageAndClassName, Lists.join(result, "\n"));
    }
}
