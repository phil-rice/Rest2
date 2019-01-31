package one.xingyi.accounts;
import one.xingyi.core.endpoints.EndpointConfig;
import one.xingyi.core.marshelling.JsonValue;

public class CertificatesApp {

    public static void main(String[] args) {
        AccountsServer<JsonValue> trafficLightServer = new AccountsServer<>(EndpointConfig.defaultConfigNoParser, new StuffManipulator());
        trafficLightServer.simpleServerWithLog(9000).start();;
        }
}
