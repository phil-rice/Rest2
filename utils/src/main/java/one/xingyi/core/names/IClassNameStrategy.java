package one.xingyi.core.names;
import one.xingyi.core.codeDom.Validators;
import one.xingyi.core.utils.Strings;
import one.xingyi.core.validation.Result;
public interface IClassNameStrategy extends INameStrategy {
    Result<String, String> toRoot(String entityType, String classSimpleName);

    IClassNameStrategy simple = new SimpleClassSimpleNameStrategy();
}

class SimpleClassSimpleNameStrategy implements IClassNameStrategy {

    @Override public Result<String, String> toRoot(String type, String className) { return Validators.removeIAndDefn(type, className); }
    @Override public String toServerCompanion(String entityDefn) { return entityDefn + "Companion"; }
    @Override public String toClientViewInterface(String entityDefn) { return entityDefn; }
    @Override public String toClientViewImpl(String entityDefn) { return entityDefn + "Impl"; }
    @Override public String toServerInterfaceForDomain(String entityDefn) { return "I" + entityDefn; }
    @Override public String toServerController(String entityDefn) { return "I" + entityDefn + "Controller"; }
    @Override public String toCompositeImpl(String compositeViewDefn) { return compositeViewDefn.substring(compositeViewDefn.length() - 4);}
    @Override public String toCompositeInterface(String compositeViewDefn) { return compositeViewDefn.substring(1, compositeViewDefn.length() - 4) + "Impl"; }
    @Override public String toClientEntityDefn(String entityDefn) { return "I" + entityDefn + "ClientEntity"; }
    @Override public String toClientCompanion(String entityDefn) { return entityDefn + "Companion"; }
    @Override public String toServerImplForDomain(String entityDefn) { return entityDefn; }
}
