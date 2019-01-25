package one.xingyi.core.codeDom;
import one.xingyi.core.utils.Lists;

import java.util.List;

public class CodeDom {

    public final List<EntityDom> entityDoms;
    public final List<ViewDom> viewDoms;
    public final List<ViewDomAndItsEntityDom> viewsAndDoms;
    public CodeDom(List<EntityDom> entityDoms, List<ViewDom> viewDoms) {
        this.entityDoms = entityDoms;
        this.viewDoms = viewDoms;
        viewsAndDoms = Lists.map(viewDoms, vd -> new ViewDomAndItsEntityDom(vd, Lists.find(entityDoms, ed -> ed.entityNames.originalDefn.equals(vd.viewNames.entityNames.originalDefn))));
    }
}
