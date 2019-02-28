package one.xingyi.core.endpoints;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.javascript.JavascriptDetailsToString;
import one.xingyi.core.javascript.JavascriptStore;
import one.xingyi.core.marshelling.*;
import one.xingyi.core.sdk.IXingYiServerCompanion;
import one.xingyi.core.utils.Files;

import java.util.List;
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class EndpointConfig<J> {
    public final String rootJavascript;
    public final JsonParserAndWriter<J> parserAndWriter;

    public final String protocol;
    public final JavascriptDetailsToString javascriptDetailsToString;
    public final IMergeJavascriptAndJson mergeJavascriptAndJson;

    public EndpointConfig<J> withMoreJavascript(String javascript) {return new EndpointConfig<>(rootJavascript + "\n\n\n" + javascript, parserAndWriter, protocol, javascriptDetailsToString, mergeJavascriptAndJson);}

//    public static EndpointConfig<JsonValue> defaultConfigNoParser = defaultConfig(JsonWriter.cheapJson);
    public static <J> EndpointConfig<J> defaultConfig(JsonParserAndWriter<J> parserAndWriter) {
        return new EndpointConfig<J>(Files.getText("header.js"), parserAndWriter, "http://", JavascriptDetailsToString.simple, IMergeJavascriptAndJson.byLinks);
    }

    public EndpointContext<J> from(List<IXingYiServerCompanion<?, ?>> companions) {
        return new EndpointContext<J>(JavascriptStore.fromEntities(rootJavascript, companions), javascriptDetailsToString, mergeJavascriptAndJson, parserAndWriter, protocol);
    }
}
