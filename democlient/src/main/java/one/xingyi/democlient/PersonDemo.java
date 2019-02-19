package one.xingyi.democlient;
import one.xingyi.certificates.client.view.IDView;
import one.xingyi.core.http.JavaHttpClient;
import one.xingyi.core.httpClient.HttpServiceCompletableFuture;
import one.xingyi.json.Json;
import one.xingyi.reference1.person.client.view.PersonLine12View;

import java.util.concurrent.ExecutionException;
public class PersonDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        HttpServiceCompletableFuture service = HttpServiceCompletableFuture.lensService("http://localhost:9000", new Json(), JavaHttpClient.client);


        PersonLine12View.getOptional(service, "id1", view ->  view.line1() + view.line2()).get();
        PersonLine12View.edit(service, "id1", view ->  view);
    }
}
