package one.xingyi.core.codeDom;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.utils.Lists;

import java.util.Optional;
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class ViewDomAndEntityDomField {
    public final FieldDom viewDomField;
    public final Optional<FieldDom> entityDomField;
    public static ViewDomAndEntityDomField create(FieldDom vdf, Optional<EntityDom> entityDom) {
        return new ViewDomAndEntityDomField(vdf, entityDom.flatMap(ed -> Lists.find(ed.fields.allFields, edf -> edf.name.equals(vdf.name))));
    }
}
