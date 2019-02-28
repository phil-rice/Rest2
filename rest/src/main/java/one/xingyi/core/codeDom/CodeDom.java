package one.xingyi.core.codeDom;
import lombok.ToString;
import one.xingyi.core.monad.MonadDefn;
import one.xingyi.core.utils.Lists;

import java.util.List;


public class CodeDom {

    public final MonadDefn monadDefn;
    public final List<ResourceDom> resourceDoms;
    public final List<ResourceDom> servedresourceDoms;
    public final List<ViewDom> viewDoms;
    public final List<ViewDomAndItsResourceDom> viewsAndDoms;
    public CodeDom(MonadDefn monadDefn, List<ResourceDom> resourceDoms, List<ViewDom> viewDoms) {
        this.monadDefn = monadDefn;
        this.resourceDoms = resourceDoms;
        this.servedresourceDoms = Lists.filter(resourceDoms, rd -> rd.bookmark.isPresent());
        this.viewDoms = viewDoms;
        viewsAndDoms = Lists.map(viewDoms, vd -> new ViewDomAndItsResourceDom(vd, Lists.find(resourceDoms, ed -> ed.entityNames.originalDefn.equals(vd.viewNames.entityNames.originalDefn))));
    }

    @Override public String toString() {
        return "CodeDom(resources=" + Lists.map(resourceDoms, d -> d.entityNames.originalDefn.className) + ",Views=" + Lists.map(viewDoms, v -> v.viewNames.originalDefn.className) + ")";
    }
}
