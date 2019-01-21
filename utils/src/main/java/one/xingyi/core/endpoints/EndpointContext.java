package one.xingyi.core.endpoints;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.javascript.JavascriptStore;
import one.xingyi.core.marshelling.JsonTC;
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class EndpointContext<J> {
    public final JavascriptStore javascriptStore;
    public final JsonTC<J> jsonTC;
}
