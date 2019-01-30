package one.xingyi.certificates;
import one.xingyi.core.endpoints.EndpointConfig;
import one.xingyi.core.marshelling.JsonValue;
public class CertificatesApp {

public static void main(String[] args){
    CertificateServer<JsonValue> trafficLightServer = new one.xingyi.certificates.CertificateServer<>(EndpointConfig.defaultConfigNoParser, new MdmAdapter());
    trafficLightServer.simpleServer(9000).start();

}
}
