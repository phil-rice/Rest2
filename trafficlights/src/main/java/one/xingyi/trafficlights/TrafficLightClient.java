package one.xingyi.trafficlights;
import one.xingyi.core.http.JavaHttpClient;
import one.xingyi.core.httpClient.HttpServiceCompletableFuture;
import one.xingyi.trafficlights.client.view.ColourView;
public class TrafficLightClient {
    public static void main(String[] args)  throws Exception {
        HttpServiceCompletableFuture service = HttpServiceCompletableFuture.defaultService("http://localhost:9000", JavaHttpClient.client);
        System.out.println(ColourView.get(service, "1", ColourView::color).get());
    }
}
