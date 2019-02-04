package one.xingyi.core.mediatype;
import one.xingyi.core.javascript.JavascriptDetailsToString;
import one.xingyi.core.javascript.JavascriptStore;
import one.xingyi.core.marshelling.JsonParserAndWriter;


public interface ServerMediaTypeContext<J> {
    JsonParserAndWriter<J> parserAndWriter();
    JavascriptStore javascriptStore();
    JavascriptDetailsToString javascriptDetailsToString();

}
