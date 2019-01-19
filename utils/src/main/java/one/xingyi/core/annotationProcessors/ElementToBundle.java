package one.xingyi.core.annotationProcessors;
import lombok.RequiredArgsConstructor;
import one.xingyi.core.names.EntityNames;
import one.xingyi.core.names.IClassNameStrategy;
import one.xingyi.core.names.IPackageNameStrategy;
import one.xingyi.core.names.IServerNames;
public interface ElementToBundle {
    public static ElementToBundle simple = new SimpleElementToBundle();
    IServerNames serverNames();
    IElementToEntityNames elementToEntityNames();
    IElementToEntityDom elementToEntityDom(EntityNames entityNames);
    IElementToFieldListDom elementToFieldListDom(EntityNames entityNames);
    IElementToFieldDom elementToFieldDom(EntityNames entityNames);
    IElementToViewDom elementToViewDom();
}
class SimpleElementToBundle implements ElementToBundle {
    @Override public IServerNames serverNames() {return IServerNames.simple(IPackageNameStrategy.simple, IClassNameStrategy.simple);}
    @Override public IElementToEntityNames elementToEntityNames() { return IElementToEntityNames.simple(serverNames()); }
    @Override public IElementToEntityDom elementToEntityDom(EntityNames entityNames) {return IElementToEntityDom.simple(this, entityNames);}
    @Override public IElementToFieldListDom elementToFieldListDom(EntityNames entityNames) {return IElementToFieldListDom.simple(this,entityNames);}
    @Override public IElementToFieldDom elementToFieldDom(EntityNames entityNames) {return IElementToFieldDom.simple(this,entityNames);}
    @Override public IElementToViewDom elementToViewDom() {return IElementToViewDom.simple;}
}
