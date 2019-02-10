package one.xingyi.certificates;
import one.xingyi.core.endpoints.EndpointConfig;
import one.xingyi.core.marshelling.JsonValue;
import one.xingyi.json.Json;

public class CertificatesApp {

    public static void main(String[] args) {
        CertificateServer<Object> trafficLightServer = new one.xingyi.certificates.CertificateServer<>(EndpointConfig.defaultConfig(new Json()), new MdmAdapter());
        trafficLightServer.simpleServerWithLog(9000).start(); ;
    }
}
