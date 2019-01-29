package one.xingyi.core.endpoints;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.javascript.JavascriptDetailsToString;
import one.xingyi.core.javascript.JavascriptStore;
import one.xingyi.core.marshelling.JsonValue;
import one.xingyi.core.marshelling.JsonParser;
import one.xingyi.core.marshelling.JsonWriter;
import one.xingyi.core.sdk.IXingYiServerCompanion;
import one.xingyi.core.utils.Files;

import java.util.List;
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class EndpointConfig<J> {
    public final String rootJavascript;
    public final JsonWriter<J> jsonWriter;
    public final JsonParser<J> jsonParser;
    public final String protocol;
    public final JavascriptDetailsToString javascriptDetailsToString;

    public static EndpointConfig<JsonValue> defaultConfigNoParser = new EndpointConfig<>(Files.getText("header.js"), JsonWriter.cheapJson, JsonParser.nullParser(), "http://", JavascriptDetailsToString.simple);

    public EndpointContext<J> from(List<IXingYiServerCompanion<?, ?>> companions) {
        return new EndpointContext<J>(JavascriptStore.fromEntities(rootJavascript, companions), javascriptDetailsToString, jsonWriter, jsonParser, protocol);
    }
}
