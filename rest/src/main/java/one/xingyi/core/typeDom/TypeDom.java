package one.xingyi.core.typeDom;
import one.xingyi.core.annotationProcessors.IViewDefnNameToViewName;
import one.xingyi.core.client.IResourceList;
import one.xingyi.core.codeDom.PackageAndClassName;
import one.xingyi.core.embedded.Embedded;
import one.xingyi.core.names.EntityNames;
import one.xingyi.core.names.IServerNames;
import one.xingyi.core.names.ViewNames;
import one.xingyi.core.utils.Strings;
import one.xingyi.core.validation.Result;

import java.util.Map;

import static one.xingyi.core.codeDom.PackageAndClassName.*;
public interface TypeDom {
    /**
     * For example if the type if List of T this is List of T
     */
    String fullTypeName();
    /**
     * This is the type that is used by entities.  For most type doms it is the same as the full typenames.
     * For view types it changes from the defn to the server interfaces
     */
    default String forEntity() {return fullTypeName();}
    /**
     * This is the type that is used by entities.  For most type doms it is the same as the full typenames.
     * For view types it changes from the defn to the view interfaces
     */
    default String forView() {return fullTypeName();}
    /**
     * empty string unless a view type
     */
    default String viewCompanion() {return "";}
    /**
     * For example if the type if List of T this is T, but if the types is T this is also T
     */
    TypeDom nested();
    boolean primitive();
    String entityNameForLens();

    default String forToJson(String fieldName, boolean templated) {return fieldName;}
    String forFromJson(String fieldName);
    String lensDefn(String fieldName);

    static Result<String, TypeDom> create(IServerNames names, String rawTypeName, IViewDefnNameToViewName viewNamesMap) {
        String fullTypeName = Strings.removeOptionalFirst("()", rawTypeName);
        PackageAndClassName packageAndClassName = new PackageAndClassName(fullTypeName);
        String listClassName = IResourceList.class.getName();
        String embeddedClassName = Embedded.class.getName();
        if (primitives().contains(packageAndClassName))
            return Result.succeed(new PrimitiveType(packageAndClassName));
        if (fullTypeName.startsWith(listClassName)) {
            String nestedName = Strings.extractFromOptionalEnvelope(listClassName + "<", ">", fullTypeName);
            return create(names, nestedName, viewNamesMap).flatMap(nested -> {
                if (viewNamesMap.isDefinedAt(nestedName)) {
                    ViewNames vn = viewNamesMap.apply(nestedName);
                    EntityNames entityNames = vn.entityNames;
                    return Result.succeed(new ListType(fullTypeName, nested, entityNames.serverCompanion, entityNames.entityNameForLens));
                } else
                    return Result.failwith("Could not work out which type " + rawTypeName + "was. Known views are\n" + viewNamesMap.legalValues());
            });
        }
        if (fullTypeName.startsWith(embeddedClassName))
            return create(names, Strings.extractFromOptionalEnvelope(embeddedClassName + "<", ">", fullTypeName), viewNamesMap).map(nested -> new EmbeddedType(fullTypeName, nested));
        if (fullTypeName.indexOf("<") == -1) {
            if (viewNamesMap.isDefinedAt(fullTypeName)) {
                ViewNames vn = viewNamesMap.apply(fullTypeName);
                EntityNames tr = vn.entityNames;
                String serviceInterface = tr.serverInterface.asString();
                return Result.succeed(new ViewType(fullTypeName, serviceInterface, vn.clientView.asString(), vn.clientCompanion.asString(), tr.serverCompanion.asString(), tr.entityNameForLens));
            }
        }
        return Result.failwith("Could not work out what type " + rawTypeName + " was. Known views are\n" + viewNamesMap.legalValues());
    }

}
