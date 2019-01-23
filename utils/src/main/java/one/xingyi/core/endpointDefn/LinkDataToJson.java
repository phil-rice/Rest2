package one.xingyi.core.endpointDefn;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.functions.Functions;
import one.xingyi.core.marshelling.ContextForJson;
import one.xingyi.core.sdk.IXingYiEntity;
import one.xingyi.core.utils.Lists;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
public interface LinkDataToJson<J, Entity extends IXingYiEntity, State> extends BiFunction<ContextForJson, Entity, List<J>> {
    static <J, Entity extends IXingYiEntity, State> LinkDataToJson<J, Entity, State> fromLinkdata(
            Function<Entity, State> stateFn,
            List<LinkData<State>> data,
            BiFunction<ContextForJson, LinkData<State>, J> toJson) {
        return new DefaultLinkDataToJson<J, Entity, State>(stateFn, data, toJson);
    }
}
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
class DefaultLinkDataToJson<J, Entity extends IXingYiEntity, State> implements LinkDataToJson<J, Entity, State> {
    final Function<Entity, State> stateFn;
    final List<LinkData<State>> data;
    final BiFunction<ContextForJson, LinkData<State>, J> toJson;

    @Override public List<J> apply(ContextForJson contextForJson, Entity entity) {
        State state = stateFn.apply(entity);
        return Lists.collect(data, d -> d.accept(state), e -> toJson.apply(contextForJson, e));
    }
}
