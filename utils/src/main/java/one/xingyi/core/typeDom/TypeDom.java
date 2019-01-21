package one.xingyi.core.typeDom;
import one.xingyi.core.codeDom.PackageAndClassName;
import one.xingyi.core.embedded.Embedded;
import one.xingyi.core.names.IServerNames;
import one.xingyi.core.utils.Strings;
import one.xingyi.core.validation.Result;

import java.util.List;

import static one.xingyi.core.codeDom.PackageAndClassName.*;
public interface TypeDom {
    /** For example if the type if List<T> this is List<T> */
    String fullTypeName();
    /** This is the type that is used by entities.  For most type doms it is the same as the full typenames.
     * For view types it changes from the defn to the server interfaces*/
    default String forEntity() {return fullTypeName();}
    /** This is the type that is used by entities.  For most type doms it is the same as the full typenames.
     * For view types it changes from the defn to the view interfaces*/
    default String forView() {return fullTypeName();}
    /** empty string unless a view type */
    default String viewCompanion() {return "";}
    /** For example if the type if List<T> this is T, but if the types is T this is also T */
    TypeDom nested();
    boolean primitive();

    default String forJson(String fieldName, boolean templated){return fieldName;}


    static Result<String, TypeDom> create(IServerNames names, String rawTypeName) {
        String fullTypeName = Strings.removeOptionalFirst("()", rawTypeName);
        PackageAndClassName packageAndClassName = new PackageAndClassName(fullTypeName);
        String listClassName = List.class.getName();
        String embeddedClassName = Embedded.class.getName();
        if (primitives().contains(packageAndClassName))
            return Result.succeed(new PrimitiveType(packageAndClassName));
        if (fullTypeName.startsWith(listClassName))
            return create(names, Strings.extractFromOptionalEnvelope(listClassName + "<", ">", fullTypeName)).map(nested -> new ListType(fullTypeName, nested));
        if (fullTypeName.startsWith(embeddedClassName))
            return create(names, Strings.extractFromOptionalEnvelope(embeddedClassName + "<", ">", fullTypeName)).map(nested -> new EmbeddedType(fullTypeName, nested));
        if (fullTypeName.indexOf("<") == -1) {
            return names.entityName(fullTypeName, "").flatMap(tr -> {
                String serviceInterface = tr.serverInterface.asString();
                String serviceClass = tr.originalDefn.asString();
                return names.viewName(fullTypeName, serviceClass, "").map(
                        vn -> new ViewType(fullTypeName, serviceInterface, vn.clientView.asString(),vn.clientCompanion.asString()));
            });
        }
        return Result.failwith("Could not work out what type " + rawTypeName + " was");
    }

}
