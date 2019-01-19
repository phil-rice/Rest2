package one.xingyi.core.typeDom;
import one.xingyi.core.codeDom.PackageAndClassName;
import one.xingyi.core.embedded.Embedded;
import one.xingyi.core.names.IServerNames;
import one.xingyi.core.utils.Strings;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static one.xingyi.core.codeDom.PackageAndClassName.*;
public interface TypeDom {
    /** For example if the type if List<T> this is List<T> */
    String fullTypeName();
    /** This is the type that should probably be used for most things. For most type doms it is the same . For view types it changes from the defn to the server interfaces*/
    default String transformed() {return fullTypeName();}
    /** For example if the type if List<T> this is T, but if the types is T this is also T */
    TypeDom nested();
    boolean primitive();


    static Set<PackageAndClassName> primitives = Set.of(stringPn, intPn, booleanPn, doublePn);

    static Optional<TypeDom> create(IServerNames names,String rawTypeName) {
        String fullTypeName = Strings.removeOptionalFirst("()", rawTypeName);
        PackageAndClassName packageAndClassName = new PackageAndClassName(fullTypeName);
        String listClassName = List.class.getName();
        String embeddedClassName = Embedded.class.getName();
        if (primitives.contains(packageAndClassName))
            return Optional.of(new PrimitiveType(packageAndClassName));
        if (fullTypeName.startsWith(listClassName))
            return create(names,Strings.extractFromOptionalEnvelope(listClassName + "<", ">", fullTypeName)).map(nested -> new ListType(fullTypeName, nested));
        if (fullTypeName.startsWith(embeddedClassName))
            return create(names,Strings.extractFromOptionalEnvelope(embeddedClassName + "<", ">", fullTypeName)).map(nested -> new EmbeddedType(fullTypeName, nested));
        if (fullTypeName.indexOf("<") == -1) {
            return names.entityName(fullTypeName, "").result().map(tr-> new ViewType(fullTypeName, tr.serverInterface.asString()));
        }

        return Optional.empty();
    }

}
