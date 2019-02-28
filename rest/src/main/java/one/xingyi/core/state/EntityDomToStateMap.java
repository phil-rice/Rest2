package one.xingyi.core.state;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.annotationProcessors.PostDom;
import one.xingyi.core.codeDom.ResourceDom;
import one.xingyi.core.utils.MapUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
public interface EntityDomToStateMap extends Function<ResourceDom, Map<String, List<StateData>>> {
     EntityDomToStateMap simple = new SimpleEntityDomToStateMap();
}
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
class SimpleEntityDomToStateMap implements EntityDomToStateMap {
    @Override public Map<String, List<StateData>> apply(ResourceDom resourceDom) {
        Map<String, List<StateData>> result = new HashMap<>();
        for (PostDom dom : resourceDom.actionsDom.postDoms) {
            for (String state : dom.states)
                MapUtils.add(result, state, new StateData(dom.action, dom.path));
        }
        return result;
    }
}

