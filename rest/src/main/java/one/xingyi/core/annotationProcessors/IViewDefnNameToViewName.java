package one.xingyi.core.annotationProcessors;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.names.ViewNames;
import one.xingyi.core.utils.PartialFunction;
import one.xingyi.core.utils.Sets;
import one.xingyi.core.utils.Strings;

import java.util.Map;
public interface IViewDefnNameToViewName extends PartialFunction<String, ViewNames> {
    static IViewDefnNameToViewName simple(Map<String, ViewNames> viewNamesMap) {return new ViewDefnNameToViewName(viewNamesMap);}

    String legalValues();
}
@RequiredArgsConstructor @ToString @EqualsAndHashCode
class ViewDefnNameToViewName implements IViewDefnNameToViewName {
    final Map<String, ViewNames> map;
    @Override public ViewNames apply(String s) {
        ViewNames viewNames = map.get(Strings.lastSegement("\\.",s));
        if (viewNames == null)
            throw new IllegalArgumentException("Cannot have argument " + s + "\nLegal values are:\n" + legalValues());
        return viewNames;
    }
    @Override public boolean isDefinedAt(String s) {
        String key = Strings.lastSegement("\\.", s);
        return map.containsKey(key);
    }
    @Override public String legalValues() {
        return Sets.sortedString(map.keySet(), "\n");
    }
}
