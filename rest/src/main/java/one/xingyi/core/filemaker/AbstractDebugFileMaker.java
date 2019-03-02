package one.xingyi.core.filemaker;
import one.xingyi.core.annotationProcessors.ActionsDom;
import one.xingyi.core.codeDom.ResourceDom;
import one.xingyi.core.codeDom.FieldDom;
import one.xingyi.core.names.EntityNames;
import one.xingyi.core.typeDom.TypeDom;
import one.xingyi.core.utils.Formating;
import one.xingyi.core.utils.Lists;
import one.xingyi.core.utils.Strings;

import java.util.List;
public class AbstractDebugFileMaker {
    List<String> typeDomInfo(TypeDom typeDom) {
        return List.of(typeDom.toString());
    }
    List<String> entityNameInfo(EntityNames names) {
        return Lists.insert(
                "Original: " + names.originalDefn.asString(),
                Formating.indent(List.of(
                        "IServer:    " + names.serverInterface.asString(),
                        "ServerImpl: " + names.serverEntity.asString(),
                        "Client:     " + names.clientResource.asString(),
                        "ServComp:   " + names.serverCompanion.asString(),
                        "Lens:       " + names.entityNameForLens
                )));
    }


    List<String> fieldDebugInfo(FieldDom fieldDom) {
        return Lists.append(
                List.<String>of("Field: " + fieldDom.name),
                Formating.indent(typeDomInfo(fieldDom.typeDom)),
                Formating.indent(List.of(
                        "Lens path  " + fieldDom.lensPath,
                        "Lens name  " + fieldDom.lensName,
                        "Readonly   " + fieldDom.readOnly,
                        "Javascript " + Strings.noWhitespace(fieldDom.javascript),
                        "Templated: " + fieldDom.templated,
                        "Deprecated " + fieldDom.deprecated)));
    }

    List<String> entityDebugInfo(ResourceDom resourceDom) {
        return Lists.<String>append(
                List.of("Deprecated: " + resourceDom.deprecated),
                entityNameInfo(resourceDom.entityNames),
                List.of(
                        "bookmarkAndUrl " + resourceDom.bookmark,
                        "Fields: " + resourceDom.fields.allFields.size()
                ),
                Formating.indent(Lists.<FieldDom, String>flatMap(resourceDom.fields.allFields, this::fieldDebugInfo))
        );
    }
    List<String> actionsDomInfo(ActionsDom actionsDom) {
        return List.of(
                "Get            " + actionsDom.getDom,
                "optionalGet    " + actionsDom.optionalGetDom,
                "Put            " + actionsDom.putDom,
                "Create         " + actionsDom.createDom,
                "CreateNoId     " + actionsDom.createWithoutIdDom,
                "Delete         " + actionsDom.deleteDom,
                "prototype:     " + actionsDom.prototypeDom,
                "prototypeNoId: " + actionsDom.prototypeNoIdDom,
                "Posts:         " + actionsDom.postDoms
        );
    }
}
