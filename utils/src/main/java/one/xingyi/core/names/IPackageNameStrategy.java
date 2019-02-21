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
    @Override public String toServerImplForDomain(String entityDefn) { return entityDefn + ".server.domain"; }
    @Override public String toServerInterfaceForDomain(String entityDefn) { return entityDefn + ".server.domain";}
    @Override public String toServerController(String entityDefn) { return entityDefn + ".server.controller"; }
    @Override public String toCompositeImpl(String compositeViewDefn) { return compositeViewDefn + ".client.compositeView"; }
    @Override public String toCompositeInterface(String compositeViewDefn) {return compositeViewDefn + ".client.compositeView";}

}
