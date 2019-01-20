package one.xingyi.core.codeDom;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.annotations.View;
import one.xingyi.core.utils.Lists;

import java.util.List;
import java.util.Optional;
@EqualsAndHashCode
@ToString
public class ViewDomAndItsEntityDom {
    public final ViewDom viewDom;
    //in a full compile it is an error to be absent. In an incremental compilation it's not unusual
    public final Optional<EntityDom> entityDom;
    public final List<ViewDomAndEntityDomField> viewAndEntityFields;

    public ViewDomAndItsEntityDom(ViewDom viewDom, Optional<EntityDom> entityDom) {
        this.viewDom = viewDom;
        this.entityDom = entityDom;
        this.viewAndEntityFields = Lists.map(viewDom.fields.allFields, vdf -> ViewDomAndEntityDomField.create(vdf, entityDom));
    }
}
