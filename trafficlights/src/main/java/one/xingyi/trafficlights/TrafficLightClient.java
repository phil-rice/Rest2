package one.xingyi.trafficlights;
import one.xingyi.core.http.JavaHttpClient;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.http.ServiceResponse;
import one.xingyi.core.httpClient.HttpServiceCompletableFuture;
import one.xingyi.trafficlights.client.view.ColourView;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
public class TrafficLightClient {
    public static void main(String[] args)  throws Exception {
        Function<ServiceRequest, CompletableFuture<ServiceResponse>> clientService = JavaHttpClient.client;
        HttpServiceCompletableFuture service = HttpServiceCompletableFuture.javascriptService("http://localhost:9000", clientService);
        System.out.println(ColourView.get(service, "1", ColourView::color).get());
    }
}
