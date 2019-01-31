package one.xingyi.democlient;
import one.xingyi.certificates.client.view.CertificateIdView;
import one.xingyi.core.http.JavaHttpClient;
import one.xingyi.core.httpClient.HttpService;
public class DemoClient {
    public static void main(String[] args) {
        HttpService service = HttpService.defaultService("http://localhost:9000", JavaHttpClient.client);
        while (true) {
            try {
                Thread.sleep(2000);
                System.out.println(

                        CertificateIdView.getOptional(service, "id1",
                        view -> view.xingYi().render("json", view) + "       ==>  " + view.id()).get());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
