package one.xingyi.core.marshelling;
import one.xingyi.core.optics.Lens;
public interface JsonParserAndWriter<J> extends JsonParser<J>, JsonWriter<J> {
    Lens<J, J> lensToChild(String childname);
    Lens<J, String> lensToString();

}
