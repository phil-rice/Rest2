package one.xingyi.core.names;
public interface INameStrategy {

    String toServerCompanion(String entityDefn);
    String toClientViewInterface(String entityDefn);
    String toClientViewImpl(String entityDefn);
    String toClientEntityDefn(String entityDefn);
    String toClientCompanion(String entityDefn);
    String toServerImplForDomain(String entityDefn);
    String toServerInterfaceForDomain(String entityDefn);
    String toServerController(String entityDefn);
    String toCompositeImpl(String compositeViewDefn);
    String toCompositeInterface(String compositeViewDefn);

}