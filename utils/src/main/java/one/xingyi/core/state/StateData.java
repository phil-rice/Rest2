package one.xingyi.core.state;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.client.ISimpleList;
import one.xingyi.core.marshelling.JsonWriter;
import one.xingyi.core.utils.Lists;
import one.xingyi.core.utils.Optionals;

import java.util.List;
import java.util.Map;
import java.util.Optional;
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class StateData {
    public final String action;
    public final String link;
    public static <J> J toJson(JsonWriter<J> writer, String state, Map<String, List<StateData>> stateMap) {
        return Optionals.<List<StateData>, J>fold(Optional.ofNullable(stateMap.get(state)),
                () -> writer.makeList(ISimpleList.create()),
                listOfStates -> writer.makeList(Lists.map(listOfStates, s -> writer.makeObject(s.action, s.link))));
    }
}
