package one.xingyi.core.codeDom;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.annotationProcessors.ActionsDom;
import one.xingyi.core.annotations.View;
import one.xingyi.core.names.IServerNames;
import one.xingyi.core.names.ViewNames;
import one.xingyi.core.utils.Strings;
import one.xingyi.core.validation.Result;

@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class ViewDom {
    //    public final EntityDom entityDom;
    public final ViewNames viewNames;
    public final FieldListDom fields;

}
