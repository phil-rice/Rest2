package one.xingyi.core.filemaker;
import one.xingyi.core.codeDom.EntityDom;
import one.xingyi.core.codeDom.FieldListDom;
import one.xingyi.core.utils.Formating;
import one.xingyi.core.utils.Lists;

import java.util.List;
public class ServerEntityFileMaker implements IFileMaker<EntityDom> {

    List<String> fields(FieldListDom dom) {
        return List.of();
    }

    List<String> constructor(FieldListDom dom) {
        return List.of();
    }

    List<String> accessors(FieldListDom dom) {
        return List.of();
    }
    List<String> hashCodeEquals(FieldListDom dom) {
        return List.of();
    }
    List<String> toString(FieldListDom dom) {
        return List.of();
    }


    @Override public FileDefn apply(EntityDom entityDom) {
        String result = Lists.join(Lists.append(
                Formating.javaFile(entityDom.entityName.serverEntity, ""),
                Formating.indent(fields(entityDom.fields)),
                Formating.indent(constructor(entityDom.fields)),
                Formating.indent(accessors(entityDom.fields)),
                Formating.indent(hashCodeEquals(entityDom.fields)),
                Formating.indent(toString(entityDom.fields)),
                List.of("/*" + entityDom + "*/"),
                List.of("}")
        ), "\n");
        return new FileDefn(entityDom.entityName.serverEntity, result);
    }
}
