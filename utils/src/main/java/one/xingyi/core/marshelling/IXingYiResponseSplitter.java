package one.xingyi.core.marshelling;
import one.xingyi.core.http.ServiceResponse;

import java.util.function.Function;
public interface IXingYiResponseSplitter extends Function<ServiceResponse, DataAndJavaScript> {

    static String marker = "\n---------\n";
    static IXingYiResponseSplitter splitter = new XingYiResponseSplitter();
}
class XingYiResponseSplitter implements IXingYiResponseSplitter {

    @Override public DataAndJavaScript apply(ServiceResponse serviceResponse) {
        if (serviceResponse.statusCode >= 300)
            throw new UnexpectedResponse(serviceResponse);
        String body = serviceResponse.body;
        int index = body.indexOf(XingYiResponseSplitter.marker);
        if (index == -1)
            throw new UnexpectedResponse("no marker found", serviceResponse);
        String javascript = body.substring(0, index);
        String data = body.substring(index + marker.length() );
        return new DataAndJavaScript(data, javascript);
    }
}