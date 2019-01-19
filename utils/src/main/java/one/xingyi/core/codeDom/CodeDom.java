package one.xingyi.core.codeDom;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import one.xingyi.core.annotations.View;

import java.util.List;
@RequiredArgsConstructor
public class CodeDom {
    public final List<EntityDom> entityDoms;
    public final List<ViewDom> viewDoms;
}
