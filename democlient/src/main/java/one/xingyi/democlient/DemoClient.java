package one.xingyi.democlient;
import one.xingyi.certificates.client.view.IDView;
import one.xingyi.core.http.JavaHttpClient;
import one.xingyi.core.httpClient.HttpServiceCompletableFuture;
public class DemoClient {
    public static void main(String[] args) {
        HttpServiceCompletableFuture service = HttpServiceCompletableFuture.defaultService("http://localhost:9000", JavaHttpClient.client);
        while (true) {
            try {
                Thread.sleep(2000);
                System.out.println(

                        IDView.getOptional(service, "id1",
                                view -> view.xingYi().render("json", view) + "       ==>  " + view.id()).get());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
