package one.xingyi.core.filemaker;
import one.xingyi.core.codeDom.*;
import one.xingyi.core.names.ViewNames;
import one.xingyi.core.utils.Formating;
import one.xingyi.core.utils.Lists;
import one.xingyi.core.utils.Optionals;
import one.xingyi.core.validation.Result;

import java.util.List;
public class ViewDomDebugFileMaker extends AbstracDebugFileMaker implements IFileMaker<ViewDomAndItsEntityDom> {

    List<String> viewNameInfo(ViewNames names) {
        return Lists.append(
                List.of("Original: " + names.originalDefn.asString()),
                Formating.indent(List.of(
                        "Entity:     " + names.clientEntity.asString(),
                        "View:        " + names.clientView.asString(),
                        "Impl   :    " + names.clientViewImpl.asString(),
                        "ServerImpl: " + names.clientCompanion.asString()
                )),
                Formating.indent(entityNameInfo(names.entityNames)));
    }
    List<String> viewDebugInfo(ViewDom viewDom) {
        return Lists.<String>append(
                viewNameInfo(viewDom.viewNames),
                List.of(
                        "Fields: " + viewDom.fields.allFields.size()
                ),
                Formating.indent(Lists.<FieldDom, String>flatMap(viewDom.fields.allFields, this::fieldDebugInfo))
        );
    }

    @Override public Result<String, FileDefn> apply(ViewDomAndItsEntityDom viewDomAndItsEntityDom) {
        ViewDom viewDom = viewDomAndItsEntityDom.viewDom;
        PackageAndClassName packageAndClassName = viewDom.viewNames.clientView.mapName(e -> e + "DebugInfo");
        List<String> manualImports = Lists.unique(viewDom.fields.map(fd -> fd.typeDom.fullTypeName()));

        List<String> result = Lists.append(
                Formating.javaFile(getClass(),viewDom.viewNames.originalDefn,"class", packageAndClassName, "", manualImports),
                List.of("/*"),

                viewDebugInfo(viewDom),
                List.of(""),
                Optionals.fold(viewDomAndItsEntityDom.entityDom, () -> List.<String>of("Entity Dom not found"), ed -> entityDebugInfo(ed)),
                List.of("*/}"));
        return Result.succeed(new FileDefn(packageAndClassName, Lists.join(result, "\n")));
    }
}
