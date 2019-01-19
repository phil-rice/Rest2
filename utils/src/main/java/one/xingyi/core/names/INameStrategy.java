package one.xingyi.core.names;
public interface INameStrategy {

    String toServerCompanion(String entityDefn);
    String toClientViews(String entityDefn);
    String toClientEntityDefn(String entityDefn);
    String toClientCompanion(String entityDefn);
    String toServerForDomain(String entityDefn);

}