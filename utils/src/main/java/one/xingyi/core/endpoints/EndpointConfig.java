package one.xingyi.core.endpoints;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.javascript.JavascriptStore;
import one.xingyi.core.marshelling.JsonWriter;
import one.xingyi.core.sdk.IXingYiServerCompanion;

import java.util.List;
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class EndpointConfig<J> {
    public final String rootJavascript;
    public final JsonWriter<J> jsonTC;
    public final String protocol;

   public EndpointContext<J> from(List<IXingYiServerCompanion<?, ?>> companions) {
        return new EndpointContext<>(JavascriptStore.fromEntities(rootJavascript, companions), jsonTC, protocol);
    }
}
