package one.xingyi.core.names;
import one.xingyi.core.utils.Strings;
import one.xingyi.core.validation.Result;

import static one.xingyi.core.validation.Valid.*;
public interface IClassNameStrategy extends INameStrategy {
    Result<String, String> toRoot(String entityType, String classSimpleName, String nameInEntityAnnotation);

    IClassNameStrategy simple = new SimpleClassSimpleNameStrategy();
}

class SimpleClassSimpleNameStrategy implements IClassNameStrategy {

    //TODO separate this out... too messy
    @Override public Result<String, String> toRoot(String type, String className, String nameInEntityAnnotation) {
        return Strings.hasContent(nameInEntityAnnotation) ?
                Result.validate(nameInEntityAnnotation,
                        check(i -> i.startsWith("I"), i -> type + " annotation [" + nameInEntityAnnotation + "] in [" + className + "] doesn't start with an I")).map(s -> s.substring(1)) :
                Result.validate(className,
                        check(i -> i.startsWith("I"), i -> type + " [" + i + "] Should start with an I"),
                        check(i -> i.endsWith("Defn"), i -> type + " [" + i + "] Should end with Defn")
                ).map(n -> Strings.from(n.substring(1, n.length() - 4), nameInEntityAnnotation));
    }
    @Override public String toServerCompanion(String entityDefn) { return entityDefn + "Companion"; }
    @Override public String toClientViewInterface(String entityDefn) { return entityDefn; }
    @Override public String toClientViewImpl(String entityDefn) { return entityDefn + "Impl"; }
    @Override public String toServerInterfaceForDomain(String entityDefn) { return "I" + entityDefn; }
    @Override public String toClientEntityDefn(String entityDefn) { return "I" + entityDefn + "ClientEntity"; }
    @Override public String toClientCompanion(String entityDefn) { return entityDefn + "Companion"; }
    @Override public String toServerImplForDomain(String entityDefn) { return entityDefn; }
}
