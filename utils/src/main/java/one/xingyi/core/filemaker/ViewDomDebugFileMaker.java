package one.xingyi.core.filemaker;
import one.xingyi.core.codeDom.EntityDom;
import one.xingyi.core.codeDom.FieldDom;
import one.xingyi.core.codeDom.PackageAndClassName;
import one.xingyi.core.codeDom.ViewDom;
import one.xingyi.core.names.EntityNames;
import one.xingyi.core.names.ViewNames;
import one.xingyi.core.typeDom.TypeDom;
import one.xingyi.core.utils.Formating;
import one.xingyi.core.utils.Lists;

import java.util.List;
public class ViewDomDebugFileMaker extends AbstracDebugFileMaker implements IFileMaker<ViewDom> {
    List<String> viewNameInfo(ViewNames names) {
        return Lists.insert(
                "Original: " + names.originalDefn.asString(),
                Formating.indent(List.of(
                        "IServer:    " + names.clientView.asString(),
                        "ServerImpl: " + names.clientCompanion.asString()
                )));
    }

    List<String> viewDebugInfo(ViewDom viewDom) {
        return Lists.<String>append(
                viewNameInfo(viewDom.viewNames),
                List.of(
                        "bookmark " + viewDom.bookmark,
                        "Fields: " + viewDom.fields.allFields.size()
                ),
                Formating.indent(Lists.<FieldDom, String>flatMap(viewDom.fields.allFields, this::fieldDebugInfo))
        );
    }

    @Override public FileDefn apply(ViewDom viewDom) {

        PackageAndClassName packageAndClassName = viewDom.viewNames.clientView.mapName(e -> e + "DebugInfo");
        List<String> result = Lists.append(
                Formating.javaFile("class", packageAndClassName, ""),
                List.of("/*"),
                viewDebugInfo(viewDom),
                List.of("*/}"));
        return new FileDefn(packageAndClassName, Lists.join(result, "\n"));
    }
}
