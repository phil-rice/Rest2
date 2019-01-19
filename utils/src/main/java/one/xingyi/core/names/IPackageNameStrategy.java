package one.xingyi.core.names;
public interface IPackageNameStrategy extends INameStrategy {


    static IPackageNameStrategy simple = new SimplePackageNameStrategy();
}

class SimplePackageNameStrategy implements IPackageNameStrategy {
    @Override public String toServerCompanion(String entityDefn) { return entityDefn + ".server.companion"; }
    @Override public String toClientViewInterface(String entityDefn) { return entityDefn + ".client.view"; }
    @Override public String toClientViewImpl(String entityDefn) { return entityDefn + ".client.view"; }
    @Override public String toClientEntityDefn(String entityDefn) { return entityDefn + ".client.entitydefn"; }
    @Override public String toClientCompanion(String entityDefn) { return entityDefn + ".client.companion"; }
    @Override public String toServerImplForDomain(String entityDefn) { return entityDefn + ".domain"; }
    @Override public String toServerInterfaceForDomain(String entityDefn) { return entityDefn + ".domain";}

}
