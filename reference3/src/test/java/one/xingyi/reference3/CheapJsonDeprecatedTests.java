package one.xingyi.reference3;
import one.xingyi.core.endpoints.EndpointConfig;
import one.xingyi.core.marshelling.JsonValue;
import one.xingyi.json.Json;
public class CheapJsonDeprecatedTests extends AbstractDeprecated3Tests<Object> {
    @Override EndpointConfig<Object> config() {
        return EndpointConfig.defaultConfig(new Json());
    }
    @Override boolean supportsReadingJson() {
        return false;
    }
}
