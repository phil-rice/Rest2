package one.xingyi.core.codeDom;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.annotations.View;
import one.xingyi.core.names.IServerNames;
import one.xingyi.core.names.ViewNames;

@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class ViewDom {
    final EntityDom entityDom;
    final ViewNames viewNames;
    final String bookmark;
    final FieldListDom fields;

    static public ViewDom create(IServerNames name, String viewInterfaceDefnName, EntityDom entityDom, View view, FieldListDom fields) {
        ViewNames viewNames = name.viewName(viewInterfaceDefnName, view.viewName());
        return new ViewDom(entityDom, viewNames, entityDom.bookmark.orElseThrow(), fields);
    }
}
