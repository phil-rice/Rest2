package one.xingyi.reference4;
import one.xingyi.core.endpoints.EndpointConfig;
import one.xingyi.core.marshelling.JsonValue;
import one.xingyi.json.Json;
public class JsonDeprecated4Tests extends AbstractDeprecated4Tests<Object> {
    @Override EndpointConfig<Object> config() {
        return EndpointConfig.defaultConfig(new Json());
    }
    @Override boolean supportsReadingJson() {
        return true;
    }
}
