package one.xingyi.core.annotationProcessors;
import lombok.RequiredArgsConstructor;
import one.xingyi.core.names.IServerNames;
import one.xingyi.core.names.ViewNames;
import one.xingyi.core.utils.Lists;
import one.xingyi.core.utils.MapUtils;
import one.xingyi.core.validation.Result;

import javax.lang.model.element.TypeElement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
public interface IElementsToMapOfViewDefnToView extends Function<List<TypeElement>, IViewDefnNameToViewName> {
    static IElementsToMapOfViewDefnToView simple(IServerNames serverNames) {return new ElementsToMapOfViewDefnToView(IElementToViewNames.simple(serverNames));}

}

@RequiredArgsConstructor
class ElementsToMapOfViewDefnToView implements IElementsToMapOfViewDefnToView {
    final IElementToViewNames elementToViewNames;

    @Override public IViewDefnNameToViewName apply(List<TypeElement> elements) {
        List<ViewNames> viewNames = Result.successes(Lists.map(elements, e -> elementToViewNames.apply(e)));
        Map<String, ViewNames> viewNamesMap = Lists.aggLeft(new HashMap<String, ViewNames>(), viewNames, (acc, vn) -> MapUtils.addNotAllowingDuplicates(acc, vn.originalDefn.className, vn, (k, v) -> "Cannot have duplicate views with the sane name " + k));
        return IViewDefnNameToViewName.simple(viewNamesMap);
    }
}
