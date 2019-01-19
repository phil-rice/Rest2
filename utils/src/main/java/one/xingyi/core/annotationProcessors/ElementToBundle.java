package one.xingyi.core.annotationProcessors;
import one.xingyi.core.names.*;
public interface ElementToBundle {
    public static ElementToBundle simple = new SimpleElementToBundle();
    IServerNames serverNames();
    IElementToEntityNames elementToEntityNames();
    IElementToEntityDom elementToEntityDom(EntityNames entityNames);
    IElementToFieldListDom elementToFieldListDomForEntity(EntityNames entityNames);
    IElementToFieldDom elementToFieldDomForEntity(EntityNames entityNames);

    IElementToViewNames elementToViewNames();
    IElementToViewDom elementToViewDom(ViewNames viewNames);
    IElementToFieldListDom elementToFieldListDomForView(ViewNames viewNames);
    IElementToFieldDom elementToFieldDomForView(ViewNames viewNames);
}
class SimpleElementToBundle implements ElementToBundle {
    @Override public IServerNames serverNames() {return IServerNames.simple(IPackageNameStrategy.simple, IClassNameStrategy.simple);}
    @Override public IElementToEntityNames elementToEntityNames() { return IElementToEntityNames.simple(serverNames()); }
    @Override public IElementToEntityDom elementToEntityDom(EntityNames entityNames) {return IElementToEntityDom.simple(this, entityNames);}
    @Override public IElementToFieldListDom elementToFieldListDomForEntity(EntityNames entityNames) {return IElementToFieldListDom.forEntities(this, entityNames);}
    @Override public IElementToFieldDom elementToFieldDomForEntity(EntityNames entityNames) {return IElementToFieldDom.forEntity(this, entityNames);}

    @Override public IElementToViewDom elementToViewDom(ViewNames viewNames) {return IElementToViewDom.forView(this, viewNames);}
    @Override public IElementToViewNames elementToViewNames() { return IElementToViewNames.simple(serverNames()); }
    @Override public IElementToFieldListDom elementToFieldListDomForView(ViewNames viewNames) { return IElementToFieldListDom.forViews(this, viewNames); }
    @Override public IElementToFieldDom elementToFieldDomForView(ViewNames viewNames) { return IElementToFieldDom.forView(this, viewNames); }
}
