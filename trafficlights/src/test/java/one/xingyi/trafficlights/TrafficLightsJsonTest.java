package one.xingyi.trafficlights;
import one.xingyi.core.endpoints.EndpointConfig;
import one.xingyi.core.http.Header;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.javascript.JavascriptDetailsToString;
import one.xingyi.core.marshelling.ContextForJson;
import one.xingyi.core.utils.Files;
import one.xingyi.core.utils.Strings;
import one.xingyi.json.Json;
import one.xingyi.trafficlights.server.domain.TrafficLights;
import org.junit.Test;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
public class TrafficLightsJsonTest {

    Json jsonParserAndWriter = new Json();

    EndpointConfig<Object> config = new EndpointConfig<>(
            Files.getText("header.js"),
            jsonParserAndWriter, jsonParserAndWriter,
            "http://",
            JavascriptDetailsToString.simple);

    String tl(String id, String colour) {
        ContextForJson context = ContextForJson.forServiceRequest("http://", new ServiceRequest("get", "/some/thing", List.of(new Header("host", "someHost")), ""));
        Object trafficLights = new TrafficLights(id, colour, "someLocation").toJsonWithLinks(jsonParserAndWriter, context, new TrafficLightsController()::stateFn);
        return jsonParserAndWriter.fromJ(trafficLights);
    }

    @Test public void testToJsonWithState() {
        assertEquals(Strings.changeQuotes("{'id':'1','color':'red','location':'someLocation','links_':[{'_self':'/some/thing'},{'orange':'{host}/lights/{id}/orange'}]}"), tl("1", "red"));
    }
}
