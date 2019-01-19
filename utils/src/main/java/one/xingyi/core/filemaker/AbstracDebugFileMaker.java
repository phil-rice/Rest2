package one.xingyi.core.filemaker;
import one.xingyi.core.codeDom.FieldDom;
import one.xingyi.core.names.EntityNames;
import one.xingyi.core.typeDom.TypeDom;
import one.xingyi.core.utils.Formating;
import one.xingyi.core.utils.Lists;

import java.util.List;
public class AbstracDebugFileMaker {
    List<String> typeDomInfo(TypeDom typeDom) {
        return List.of(typeDom.toString());
    }


    List<String> fieldDebugInfo(FieldDom fieldDom) {
        return Lists.append(
                List.<String>of("Field: " + fieldDom.name),
                Formating.indent(typeDomInfo(fieldDom.typeDom)),
                Formating.indent(List.of(
                        "Lens path " + fieldDom.lensPath,
                        "Lens name " + fieldDom.lensName,
                        "Readonly " + fieldDom.readOnly
                )));
    }

}
