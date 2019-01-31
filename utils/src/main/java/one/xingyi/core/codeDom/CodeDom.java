package one.xingyi.core.codeDom;
import one.xingyi.core.utils.Lists;

import java.util.List;

public class CodeDom {

    public final List<ResourceDom> resourceDoms;
    public final List<ViewDom> viewDoms;
    public final List<ViewDomAndItsResourceDom> viewsAndDoms;
    public CodeDom(List<ResourceDom> resourceDoms, List<ViewDom> viewDoms) {
        this.resourceDoms = resourceDoms;
        this.viewDoms = viewDoms;
        viewsAndDoms = Lists.map(viewDoms, vd -> new ViewDomAndItsResourceDom(vd, Lists.find(resourceDoms, ed -> ed.entityNames.originalDefn.equals(vd.viewNames.entityNames.originalDefn))));
    }
}
