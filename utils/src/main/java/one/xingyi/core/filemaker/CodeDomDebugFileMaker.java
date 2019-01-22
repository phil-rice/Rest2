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

    @Override public FileDefn apply(EntityDom entityDom) {
        PackageAndClassName packageAndClassName = entityDom.entityName.serverEntity.mapName(e -> e + "DebugInfo");
        List<String> result = Lists.append(
                Formating.javaFile(getClass(),entityDom.entityName.originalDefn,"class", packageAndClassName, "", List.of()),
                List.of("/*"),
                entityDebugInfo(entityDom),
                List.of("*/}"));
        return new FileDefn(packageAndClassName, Lists.join(result, "\n"));
    }
}
