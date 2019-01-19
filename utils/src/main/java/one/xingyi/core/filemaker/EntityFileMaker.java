package one.xingyi.core.filemaker;
import one.xingyi.core.codeDom.EntityDom;
public class EntityFileMaker implements IFileMaker<EntityDom> {
    @Override public FileDefn apply(EntityDom entityDom) {
                return new FileDefn(entityDom.entityName.serverEntity, "some content based on " + entityDom);
    }
}
