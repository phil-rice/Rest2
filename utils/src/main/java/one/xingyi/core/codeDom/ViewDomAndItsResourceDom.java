package one.xingyi.core.codeDom;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import one.xingyi.core.utils.Lists;

import java.util.List;
import java.util.Optional;
@EqualsAndHashCode
@ToString
public class ViewDomAndItsResourceDom {
    public final ViewDom viewDom;
    //in a full compile it is an error to be absent. In an incremental compilation it's not unusual
    public final Optional<ResourceDom> entityDom;
    public final List<ViewDomAndResourceDomField> viewDomAndResourceDomFields;

    public ViewDomAndItsResourceDom(ViewDom viewDom, Optional<ResourceDom> resourceDom) {
        this.viewDom = viewDom;
        this.entityDom = resourceDom;
        this.viewDomAndResourceDomFields = Lists.map(viewDom.fields.allFields, vdf -> ViewDomAndResourceDomField.create(vdf, resourceDom));
    }
}
