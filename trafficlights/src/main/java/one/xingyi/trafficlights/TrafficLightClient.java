package one.xingyi.trafficlights;
import one.xingyi.core.http.JavaHttpClient;
import one.xingyi.core.httpClient.HttpService;
import one.xingyi.trafficlights.client.view.ColourView;

import java.util.concurrent.ExecutionException;
public class TrafficLightClient {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        HttpService service = HttpService.defaultService("http://localhost:9000", JavaHttpClient.client);
        System.out.println(ColourView.get(service, "1", ColourView::color).get());
    }
}
