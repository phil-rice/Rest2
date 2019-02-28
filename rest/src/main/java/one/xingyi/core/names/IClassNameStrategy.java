package one.xingyi.core.names;
import one.xingyi.core.utils.Strings;
public interface IClassNameStrategy extends INameStrategy {
    String toRoot(String entityType);

    IClassNameStrategy simple = new SimpleClassSimpleNameStrategy();
}

class SimpleClassSimpleNameStrategy implements IClassNameStrategy {

    @Override public String toRoot(String type) { return Strings.extractFromOptionalEnvelope("I", "Defn", type);}
    @Override public String toServerCompanion(String entityDefn) { return entityDefn + "Companion"; }
    @Override public String toClientViewInterface(String entityDefn) { return entityDefn; }
    @Override public String toClientViewImpl(String entityDefn) { return entityDefn + "Impl"; }
    @Override public String toServerInterfaceForDomain(String entityDefn) { return "I" + entityDefn; }
    @Override public String toServerController(String entityDefn) { return "I" + entityDefn + "Controller"; }
    @Override public String toCompositeImpl(String compositeViewDefn) { return compositeViewDefn.substring(1, compositeViewDefn.length() - 4) + "Impl";}
    @Override public String toCompositeInterface(String compositeViewDefn) { return compositeViewDefn.substring(0, compositeViewDefn.length() - 4); }
    @Override public String toClientEntityDefn(String entityDefn) { return "I" + entityDefn + "ClientEntity"; }
    @Override public String toClientViewCompanion(String entityDefn) { return entityDefn + "Companion"; }
    @Override public String toCompositeCompanion(String entityDefn) { return entityDefn + "CompositeCompanion"; }
    @Override public String toServerImplForDomain(String entityDefn) { return entityDefn; }
}
