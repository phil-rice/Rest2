package one.xingyi.core.endpoints;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.javascript.JavascriptDetailsToString;
import one.xingyi.core.javascript.JavascriptStore;
import one.xingyi.core.marshelling.IXingYiResponseSplitter;
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
    public final IMergeJavascriptAndJson mergeJavascriptAndJson;

    public EndpointConfig<J> withMoreJavascript(String javascript) {return new EndpointConfig<>(rootJavascript + "\n\n\n" + javascript, jsonWriter, jsonParser, protocol, javascriptDetailsToString, mergeJavascriptAndJson);}

    public static EndpointConfig<JsonValue> defaultConfigNoParser = defaultConfig(JsonWriter.cheapJson, JsonParser.nullParser());
    public static <J> EndpointConfig<J> defaultConfig(JsonWriter<J> jsonWriter, JsonParser<J> jsonParser) {
        return new EndpointConfig<>(Files.getText("header.js"), jsonWriter, jsonParser, "http://", JavascriptDetailsToString.simple, IMergeJavascriptAndJson.byLinks);
    }

    public EndpointContext<J> from(List<IXingYiServerCompanion<?, ?>> companions) {
        return new EndpointContext<J>(JavascriptStore.fromEntities(rootJavascript, companions), javascriptDetailsToString, mergeJavascriptAndJson, jsonWriter, jsonParser, protocol);
    }
}
