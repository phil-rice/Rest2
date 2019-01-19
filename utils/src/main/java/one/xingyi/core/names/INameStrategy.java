package one.xingyi.core.names;
public interface INameStrategy {

    String toServerCompanion(String entityDefn);
    String toClientViewInterface(String entityDefn);
    String toClientViewImpl(String entityDefn);
    String toClientEntityDefn(String entityDefn);
    String toClientCompanion(String entityDefn);
    String toServerImplForDomain(String entityDefn);
    String toServerInterfaceForDomain(String entityDefn);

}