package one.xingyi.core.codeDom;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.annotations.View;
import one.xingyi.core.names.IServerNames;
import one.xingyi.core.names.ViewNames;
import one.xingyi.core.validation.Result;

@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class ViewDom {
    final EntityDom entityDom;
    final ViewNames viewNames;
    final String bookmark;
    final FieldListDom fields;

    static public Result<String, ViewDom> create(IServerNames name, String viewInterfaceDefnName, EntityDom entityDom, View view, FieldListDom fields) {
        return name.viewName(viewInterfaceDefnName, view.viewName()).map(viewNames -> new ViewDom(entityDom, viewNames, entityDom.bookmark.orElseThrow(), fields));
    }
}
