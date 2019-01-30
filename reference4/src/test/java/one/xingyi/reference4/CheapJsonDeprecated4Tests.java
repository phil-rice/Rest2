package one.xingyi.reference4;
import one.xingyi.core.endpoints.EndpointConfig;
import one.xingyi.core.marshelling.JsonValue;
public class CheapJsonDeprecated4Tests extends AbstractDeprecated4Tests<JsonValue> {
    @Override EndpointConfig<JsonValue> config() {
        return EndpointConfig.defaultConfigNoParser;
    }
    @Override boolean supportsReadingJson() {
        return false;
    }
}
