package one.xingyi.reference3;
import one.xingyi.core.endpoints.EndpointConfig;
import one.xingyi.core.marshelling.JsonValue;
public class CheapJsonDeprecatedTests extends AbstractDeprecated3Tests<JsonValue> {
    @Override EndpointConfig<JsonValue> config() {
        return EndpointConfig.defaultConfigNoParser;
    }
    @Override boolean supportsReadingJson() {
        return false;
    }
}
