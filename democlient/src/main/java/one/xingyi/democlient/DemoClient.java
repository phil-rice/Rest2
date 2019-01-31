package one.xingyi.democlient;
import one.xingyi.accounts.client.view.AccountsIdView;
import one.xingyi.certificates.client.view.IDView;
import one.xingyi.core.http.JavaHttpClient;
import one.xingyi.core.httpClient.HttpService;
public class DemoClient {
    public static void main(String[] args) {
        HttpService service = HttpService.defaultService("http://localhost:9000", JavaHttpClient.client);
        while (true) {
            try {
                Thread.sleep(2000);

                System.out.println(AccountsIdView.get(service, "anyoldid", v -> v.id()).get());
//                AccountsIdView.edit(service, "asd", v-> v.withid("someNewId"));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
