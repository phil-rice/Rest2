package one.xingyi.core.endpoints;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.javascript.JavascriptDetailsToString;
import one.xingyi.core.javascript.JavascriptStore;
import one.xingyi.core.marshelling.JsonObject;
import one.xingyi.core.marshelling.JsonWriter;
import one.xingyi.core.sdk.IXingYiServerCompanion;
import one.xingyi.core.utils.Files;

import java.util.List;
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class EndpointConfig<J> {
    public final String rootJavascript;
    public final JsonWriter<J> jsonTC;
    public final String protocol;
    public final JavascriptDetailsToString javascriptDetailsToString;

    public static EndpointConfig<JsonObject> defaultConfig = new EndpointConfig<>(Files.getText("header.js"), JsonWriter.cheapJson, "http://", JavascriptDetailsToString.simple);

    public EndpointContext from(List<IXingYiServerCompanion<?, ?>> companions) {
        return new EndpointContext<J>(JavascriptStore.fromEntities(rootJavascript, companions), javascriptDetailsToString, jsonTC, protocol);
    }
}
