package one.xingyi.core.typeDom;
import one.xingyi.core.annotationProcessors.IViewDefnNameToViewName;
import one.xingyi.core.client.IResourceList;
import one.xingyi.core.client.ISimpleList;
import one.xingyi.core.codeDom.FieldDom;
import one.xingyi.core.codeDom.PackageAndClassName;
import one.xingyi.core.embedded.Embedded;
import one.xingyi.core.names.EntityNames;
import one.xingyi.core.names.IServerNames;
import one.xingyi.core.names.ViewNames;
import one.xingyi.core.utils.PartialFunction;
import one.xingyi.core.utils.Strings;
import one.xingyi.core.validation.Result;

import java.util.List;
import java.util.Map;

import static one.xingyi.core.codeDom.PackageAndClassName.*;
import static one.xingyi.core.utils.PartialFunction.*;
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
    String makeLens(PackageAndClassName companion, String interfaceName, FieldDom viewDom, String lensName);

    default String forToJson(String fieldName, boolean templated) {return fieldName;}
    String forFromJson(String fieldName);
    String lensDefn(String fieldName);

    PartialFunction<String, Result<String, TypeDom>> fromPrimitive =
            pf(fullTypeName -> primitives().contains(new PackageAndClassName(fullTypeName)), fullTypeName -> {
                return Result.succeed(new PrimitiveType(new PackageAndClassName(fullTypeName)));
            });

//    PartialFunction<String, Result<String, TypeDom>> fromSimpleList =
//            pf(fullTypeName -> fullTypeName.startsWith(ISimpleList.class.getName()), fullTypeName -> {
//                String nested = Strings.extractFromOptionalEnvelope(ISimpleList.class.getName() + "<", ">", fullTypeName);
//                if (primitives().contains(nested)) {
//                    return Result.succeed(new SimpleListType(fullTypeName, new PrimitiveType(new PackageAndClassName(nested))));
//                } else
//                    return Result.failwith("Could not make a " + fullTypeName + " simple lists must hold primitives. Legal values: " + primitives());
//            });

    static PartialFunction<String, Result<String, TypeDom>> fromEmbedded(IServerNames names, IViewDefnNameToViewName viewNamesMap) {
        return pf(fullTypeName -> fullTypeName.startsWith(Embedded.class.getName()),
                fullTypeName -> create(names, Strings.extractFromOptionalEnvelope(Embedded.class.getName() + "<", ">", fullTypeName), viewNamesMap).
                        map(nested -> new EmbeddedType(fullTypeName, nested)));
    }

    static PartialFunction<String, Result<String, TypeDom>> fromResourceList(IServerNames names, IViewDefnNameToViewName viewNamesMap) {
        return pf(fullTypeName -> fullTypeName.startsWith(IResourceList.class.getName()),
                fullTypeName -> {
                    String nestedName = Strings.extractFromOptionalEnvelope(IResourceList.class.getName() + "<", ">", fullTypeName);
                    return create(names, nestedName, viewNamesMap).flatMap(nested -> {
                        if (viewNamesMap.isDefinedAt(nestedName)) {
                            ViewNames vn = viewNamesMap.apply(nestedName);
                            EntityNames entityNames = vn.entityNames;
                            return Result.succeed(new ListType(fullTypeName, nested, entityNames.serverCompanion, entityNames.entityNameForLens));
                        } else
                            return Result.failwith("Could not work out which type " + fullTypeName + "was. Known views are\n" + viewNamesMap.legalValues());
                    });
                });
    }

    static PartialFunction<String, Result<String, TypeDom>> fromView(IViewDefnNameToViewName viewNamesMap) {
        return pf(fullTypeName -> viewNamesMap.isDefinedAt(fullTypeName) && fullTypeName.indexOf("<") == -1,
                fullTypeName -> {
                    ViewNames vn = viewNamesMap.apply(fullTypeName);
                    EntityNames tr = vn.entityNames;
                    String serviceInterface = tr.serverInterface.asString();
                    return Result.succeed(new ViewType(fullTypeName, serviceInterface, vn.clientView.asString(), vn.clientCompanion.asString(), tr.serverCompanion.asString(), tr.entityNameForLens));
                });
    }


    static Result<String, TypeDom> create(IServerNames names, String rawTypeName, IViewDefnNameToViewName viewNamesMap) {
        String fullTypeName = Strings.removeOptionalFirst("()", rawTypeName);
        PartialFunction<String, Result<String, TypeDom>> pf = chain(fromPrimitive, fromEmbedded(names, viewNamesMap), fromResourceList(names, viewNamesMap), fromView(viewNamesMap));
        return pf.orDefault(fullTypeName, () -> Result.failwith("Could not work out what type " + fullTypeName + " was. Known views are\n" + viewNamesMap.legalValues()));
    }

    boolean isAssignableFrom(TypeDom other);
}
